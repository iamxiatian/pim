package xiatian.pim.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xiatian.pim.domain.Contact;
import xiatian.pim.domain.Journal;
import xiatian.pim.domain.PropertyHandler;
import xiatian.pim.domain.Todo;
import xiatian.pim.util.BlankUtils;
import xiatian.pim.util.FileUtils;

public class PimDb {
    private static PimDb db = null;
    private Connection conn = null;

    private static ThreadLocal<SimpleDateFormat> fullDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static String getDateString(java.util.Date date) {
        return fullDateFormat.get().format(date);
    }

    public static java.util.Date parseDate(String dateString) throws Exception {
        return fullDateFormat.get().parse(dateString);
    }

    public static PimDb getInstance() {
        if (db == null) {
            db = new PimDb();
        }
        return db;
    }

    public void open(String dbname) throws Exception {
        // File f = new File(dbname);
        // boolean isNewDb = !f.exists();
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbname);
        conn.setAutoCommit(true);

        // if (isNewDb)
        setup();
    }

    public void close() throws Exception {
        conn.close();
    }

    /**
     * 创建数据库
     * 
     * @throws Exception
     */
    public void setup() throws Exception {
        Statement st = conn.createStatement();
//        st.executeUpdate("ALTER TABLE journal ADD COLUMN properties TEXT");
        st.executeUpdate("create table IF NOT EXISTS journal (id INTEGER PRIMARY KEY, parentId INTEGER, position INTEGER,state INTEGER, password TEXT, title TEXT, tags TEXT, content TEXT, createDate DATE, properties TEXT)");
        // st.executeUpdate("drop table contact");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS contact(id INTEGER PRIMARY KEY,  name varchar(20), position INTEGER,state INTEGER, telephone TEXT, cellphone TEXT, email TEXT, category varchar(20), organization TEXT, addTime datetime, memo text)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS todolist(id INTEGER PRIMARY KEY,  name varchar(20), category varchar(20),addTime DATETIME, startTime DATETIME, endTime DATETIME, finishTime DATETIME INTEGER,state INTEGER, memo text)");

        st.close();
    }

    // /////////////////////////////////////////////////////////
    // TODO Processing
    // /////////////////////////////////////////////////////////
    public void exportTodolist(File xmlFile) throws Exception {
        List<Todo> list = getTodoList();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("totolist");
        document.appendChild(root);
        for (Todo todo : list) {
            Element todoNode = document.createElement("todo");
            root.appendChild(todoNode);

            Element nameNode = document.createElement("name");
            nameNode.appendChild(document.createTextNode(todo.getName()));
            todoNode.appendChild(nameNode);

            Element categoryNode = document.createElement("category");
            categoryNode.appendChild(document.createTextNode(todo.getCategory()));
            todoNode.appendChild(categoryNode);

            Element addTimeNode = document.createElement("addTime");
            addTimeNode.appendChild(document.createTextNode(getDateString(todo.getAddTime())));
            todoNode.appendChild(addTimeNode);

            Element n = document.createElement("startTime");
            n.appendChild(document.createTextNode(getDateString(todo.getStartTime())));
            todoNode.appendChild(n);

            n = document.createElement("endTime");
            n.appendChild(document.createTextNode(getDateString(todo.getEndTime())));
            todoNode.appendChild(n);

            n = document.createElement("finishTime");
            n.appendChild(document.createTextNode(getDateString(todo.getFinishTime())));
            todoNode.appendChild(n);

            n = document.createElement("state");
            n.appendChild(document.createTextNode(Integer.toString(todo.getState())));
            todoNode.appendChild(n);

            Element memoNode = document.createElement("memo");
            memoNode.appendChild(document.createTextNode(todo.getMemo()));
            todoNode.appendChild(memoNode);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        PrintWriter pw = new PrintWriter(new FileOutputStream(xmlFile));
        StreamResult result = new StreamResult(pw);
        transformer.transform(source, result);
    }

    /**
     * 注意第一个节点不能是注释节点
     * 
     * @param xmlFile
     * @return
     * @throws Exception
     */
    public int importTodolist(File xmlFile) throws Exception {
        int count = 0;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();
        Node node = document.getFirstChild();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node nodeitm = list.item(i);
            if (nodeitm.getNodeName().equalsIgnoreCase("todo")) {
                NodeList children = nodeitm.getChildNodes();
                Todo todo = new Todo();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if (child.getNodeName().equalsIgnoreCase("name")) {
                        todo.setName(child.getFirstChild().getNodeValue());
                    } else if (child.getNodeName().equalsIgnoreCase("category")) {
                        todo.setCategory(child.getFirstChild().getNodeValue());
                    } else if (child.getNodeName().equalsIgnoreCase("addTime")) {
                        todo.setAddTime(parseDate(child.getFirstChild().getNodeValue()));
                    } else if (child.getNodeName().equalsIgnoreCase("startTime")) {
                        todo.setStartTime(parseDate(child.getFirstChild().getNodeValue()));
                    } else if (child.getNodeName().equalsIgnoreCase("endTime")) {
                        todo.setEndTime(parseDate(child.getFirstChild().getNodeValue()));
                    } else if (child.getNodeName().equalsIgnoreCase("finishTime")) {
                        todo.setFinishTime(parseDate(child.getFirstChild().getNodeValue()));
                    } else if (child.getNodeName().equalsIgnoreCase("memo")) {
                        todo.setMemo(getNodeTextValue(child));
                    } else if (child.getNodeName().equalsIgnoreCase("state")) {
                        todo.setState(Integer.parseInt(child.getFirstChild().getNodeValue()));
                    }
                }
                if (todo.getName() != null) {
                    count++;
                    addTodo(todo);
                }
            }
        }
        return count;
    }

    public List<Todo> getTodoList() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Todo> list = new ArrayList<Todo>();

        try {
            ps = conn.prepareStatement("SELECT * from todolist ORDER BY endTime DESC");
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                Todo todo = new Todo();
                todo.setId(rs.getInt("id"));
                todo.setName(rs.getString("name"));
                todo.setCategory(rs.getString("category"));
                todo.setAddTime(rs.getDate("addTime"));
                todo.setStartTime(rs.getDate("startTime"));
                todo.setEndTime(rs.getDate("endTime"));
                todo.setFinishTime(rs.getDate("finishTime"));
                todo.setMemo(rs.getString("memo"));
                todo.setState(rs.getInt("state"));
                list.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public boolean removeTodo(int id) {
        PreparedStatement ps = null;
        int count = 0;

        try {
            ps = conn.prepareStatement("delete from todolist WHERE id=?");
            ps.setInt(1, id);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count == 1;
    }

    public Todo getTodo(int id) {
        Todo todo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * from todolist WHERE id=" + id);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                todo = new Todo();
                todo.setId(rs.getInt("id"));
                todo.setName(rs.getString("name"));
                todo.setCategory(rs.getString("category"));
                todo.setAddTime(rs.getDate("addTime"));
                todo.setStartTime(rs.getDate("startTime"));
                todo.setEndTime(rs.getDate("endTime"));
                todo.setFinishTime(rs.getDate("finishTime"));
                todo.setMemo(rs.getString("memo"));
                todo.setState(rs.getInt("state"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return todo;
    }

    public boolean updateTodo(Todo todo) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            ps = conn.prepareStatement("UPDATE todolist set name=?, category=?, startTime=?, endTime=?, finishTime=?, memo=?, state=? WHERE id=?");
            ps.setString(1, todo.getName());
            ps.setString(2, todo.getCategory());
            ps.setDate(3, new Date(todo.getStartTime().getTime()));
            ps.setDate(4, new Date(todo.getEndTime().getTime()));
            ps.setDate(5, new Date(todo.getFinishTime().getTime()));
            ps.setString(6, todo.getMemo());
            ps.setInt(7, todo.getState());
            ps.setInt(8, todo.getId());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count == 1;
    }

    public int addTodo(Todo todo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement st = null;

        int lastId = 0;
        try {
            ps = conn
                    .prepareStatement("insert into todolist (id, name, category, addTime, startTime, endTime, finishTime, state, memo) values (NULL, ?, ?, ?, ?,?, ?, ?, ?)");
            ps.setString(1, todo.getName());
            ps.setString(2, todo.getCategory());
            ps.setDate(3, new Date(todo.getAddTime().getTime()));
            ps.setDate(4, new Date(todo.getStartTime().getTime()));
            ps.setDate(5, new Date(todo.getEndTime().getTime()));
            ps.setDate(6, new Date(todo.getFinishTime().getTime()));
            ps.setInt(7, todo.getState());
            ps.setString(8, todo.getMemo());
            ps.executeUpdate();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT last_insert_rowid()");
            rs.next();
            lastId = rs.getInt(1);
            todo.setId(lastId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lastId;
    }

    // /////////////////////////////////////////////////////////
    // AddressList Processing
    // /////////////////////////////////////////////////////////
    public void exportContacts(File xmlFile) throws Exception {
        List<Contact> list = getContactList();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("contacts");
        document.appendChild(root);
        for (Contact c : list) {
            Element contactNode = document.createElement("contact");
            root.appendChild(contactNode);

            Element nameNode = document.createElement("name");
            nameNode.appendChild(document.createTextNode(c.getName()));
            contactNode.appendChild(nameNode);

            Element cellNode = document.createElement("cellphone");
            cellNode.appendChild(document.createTextNode(c.getCellphone()));
            contactNode.appendChild(cellNode);

            Element telephoneNode = document.createElement("telephone");
            telephoneNode.appendChild(document.createTextNode(c.getTelephone()));
            contactNode.appendChild(telephoneNode);

            Element emailNode = document.createElement("email");
            emailNode.appendChild(document.createTextNode(c.getEmail()));
            contactNode.appendChild(emailNode);

            Element categoryNode = document.createElement("category");
            categoryNode.appendChild(document.createTextNode(c.getCategory()));
            contactNode.appendChild(categoryNode);

            Element orgNode = document.createElement("organization");
            orgNode.appendChild(document.createTextNode(c.getOrganization()));
            contactNode.appendChild(orgNode);

            Element memoNode = document.createElement("memo");
            memoNode.appendChild(document.createTextNode(c.getMemo()));
            contactNode.appendChild(memoNode);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        PrintWriter pw = new PrintWriter(new FileOutputStream(xmlFile));
        StreamResult result = new StreamResult(pw);
        transformer.transform(source, result);
    }

    private String getNodeTextValue(Node node) {
        if (node == null)
            return "";
        if (node.getFirstChild() == null) {
            return "";
        } else {
            return node.getFirstChild().getNodeValue();
        }
    }

    /**
     * 注意第一个节点不能是注释节点
     * 
     * @param xmlFile
     * @return
     * @throws Exception
     */
    public int importContacts(File xmlFile) throws Exception {
        int count = 0;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();
        Node node = document.getFirstChild();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node nodeitm = list.item(i);
            if (nodeitm.getNodeName().equalsIgnoreCase("contact")) {
                NodeList children = nodeitm.getChildNodes();
                Contact c = new Contact();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if (child.getNodeName().equalsIgnoreCase("name")) {
                        c.setName(child.getFirstChild().getNodeValue());
                    } else if (child.getNodeName().equalsIgnoreCase("cellphone")) {
                        c.setCellphone(getNodeTextValue(child));
                    } else if (child.getNodeName().equalsIgnoreCase("telephone")) {
                        c.setTelephone(getNodeTextValue(child));
                    } else if (child.getNodeName().equalsIgnoreCase("email")) {
                        c.setEmail(getNodeTextValue(child));
                    } else if (child.getNodeName().equalsIgnoreCase("category")) {
                        c.setCategory(getNodeTextValue(child));
                    } else if (child.getNodeName().equalsIgnoreCase("memo")) {
                        c.setMemo(getNodeTextValue(child));
                    } else if (child.getNodeName().equalsIgnoreCase("organization")) {
                        c.setOrganization(getNodeTextValue(child));
                    }
                }
                if (c.getName() != null) {
                    count++;
                    addContact(c);
                }
            }
        }
        return count;
    }

    public List<String> getContactCategoryList() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<String>();

        try {
            ps = conn.prepareStatement("SELECT distinct category from contact ORDER BY category");
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                list.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<Contact> getContactList(String category) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Contact> list = new ArrayList<Contact>();

        try {
            ps = conn.prepareStatement("SELECT * from contact WHERE category LIKE ?");
            ps.setString(1, category);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                Contact c = new Contact();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setTelephone(rs.getString("telephone"));
                c.setCellphone(rs.getString("cellphone"));
                c.setEmail(rs.getString("email"));
                c.setCategory(rs.getString("category"));
                c.setAddTime(rs.getDate("addTime"));
                c.setMemo(rs.getString("memo"));
                c.setOrganization(rs.getString("organization"));
                c.setState(rs.getInt("state"));
                c.setPosition(rs.getInt("position"));

                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<Contact> getContactList() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Contact> list = new ArrayList<Contact>();

        try {
            ps = conn.prepareStatement("SELECT * from contact ORDER BY category");
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                Contact c = new Contact();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setTelephone(rs.getString("telephone"));
                c.setCellphone(rs.getString("cellphone"));
                c.setEmail(rs.getString("email"));
                c.setCategory(rs.getString("category"));
                c.setAddTime(rs.getDate("addTime"));
                c.setMemo(rs.getString("memo"));
                c.setOrganization(rs.getString("organization"));
                c.setState(rs.getInt("state"));
                c.setPosition(rs.getInt("position"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public boolean removeContact(int id) {
        PreparedStatement ps = null;
        int count = 0;

        try {
            ps = conn.prepareStatement("delete from contact WHERE id=?");
            ps.setInt(1, id);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count == 1;
    }

    public Contact getContact(int id) {
        Contact c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * from contact WHERE id=" + id);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                c = new Contact();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setTelephone(rs.getString("telephone"));
                c.setCellphone(rs.getString("cellphone"));
                c.setEmail(rs.getString("email"));
                c.setCategory(rs.getString("category"));
                c.setAddTime(rs.getDate("addTime"));
                c.setMemo(rs.getString("memo"));
                c.setOrganization(rs.getString("organization"));
                c.setState(rs.getInt("state"));
                c.setPosition(rs.getInt("position"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return c;
    }

    public boolean updateContact(Contact c) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            ps = conn
                    .prepareStatement("UPDATE contact set name=?, telephone=?, cellphone=?, email=?, category=?, organization=?, memo=?, state=?, position=? WHERE id=?");
            ps.setString(1, c.getName());
            ps.setString(2, c.getTelephone());
            ps.setString(3, c.getCellphone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getCategory());
            ps.setString(6, c.getOrganization());
            ps.setString(7, c.getMemo());
            ps.setInt(8, c.getState());
            ps.setInt(9, c.getPosition());
            ps.setInt(10, c.getId());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count == 1;
    }

    public int addContact(Contact c) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement st = null;

        int lastId = 0;
        try {
            ps = conn
                    .prepareStatement("insert into contact (id, name, position, state, telephone, cellphone, email, category, organization, addTime, memo) values (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, c.getName());
            ps.setInt(2, c.getPosition());
            ps.setInt(3, c.getState());
            ps.setString(4, c.getTelephone());
            ps.setString(5, c.getCellphone());
            ps.setString(6, c.getEmail());
            ps.setString(7, c.getCategory());
            ps.setString(8, c.getOrganization());
            ps.setDate(9, new Date(c.getAddTime().getTime()));
            ps.setString(10, c.getMemo());
            ps.executeUpdate();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT last_insert_rowid()");
            rs.next();
            lastId = rs.getInt(1);
            c.setId(lastId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lastId;
    }

    // /////////////////////////////////////////////////////////
    // Journal Processing
    // /////////////////////////////////////////////////////////
    public List<Journal> getJourlnalList(int parentId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Journal> list = new ArrayList<Journal>();

        try {
            ps = conn.prepareStatement("SELECT * from journal WHERE parentId=" + parentId + " ORDER BY position");
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                Journal j = new Journal();
                j.setId(rs.getInt("id"));
                j.setParentId(rs.getInt("parentId"));
                j.setPosition(rs.getInt("position"));
                j.setState(rs.getInt("state"));
                j.setPassword(rs.getString("password"));
                j.setTitle(rs.getString("title"));
                j.setTags(rs.getString("tags"));
                j.setContent(rs.getString("content"));
                j.setCreateDate(rs.getDate("createDate"));
                j.setProperties(rs.getString("properties"));
                list.add(j);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public int getJournalChildrenCount(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT count(id) from journal WHERE parentId=" + id);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public boolean removeJournal(int id) {
        PreparedStatement ps = null;
        int count = 0;

        try {
            ps = conn.prepareStatement("delete from journal WHERE id=?");
            ps.setInt(1, id);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count == 1;
    }

    public void removeRecursiveJournal(int id) {
        List<Journal> list = getJourlnalList(id);
        removeJournal(id);
        for (Journal j : list) {
            removeRecursiveJournal(j.getId());
        }
    }

    public Journal getJourlnal(int id) {
        if (id == 0) {
            return new Journal("My Notes");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * from journal WHERE id=" + id);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                Journal j = new Journal();
                j.setId(rs.getInt("id"));
                j.setParentId(rs.getInt("parentId"));
                j.setPosition(rs.getInt("position"));
                j.setState(rs.getInt("state"));
                j.setPassword(rs.getString("password"));
                j.setTitle(rs.getString("title"));
                j.setTags(rs.getString("tags"));
                j.setContent(rs.getString("content"));
                j.setCreateDate(rs.getDate("createDate"));
                j.setProperties(rs.getString("properties"));

                return j;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean updateJournalState(int journalId, int state) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            ps = conn.prepareStatement("UPDATE journal set state=? WHERE id=?");
            ps.setInt(1, state);
            ps.setInt(2, journalId);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count == 1;
    }

    public boolean updateJournalContent(int journalId, String newContent) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            ps = conn.prepareStatement("UPDATE journal set content=? WHERE id=?");
            ps.setString(1, newContent);
            ps.setInt(2, journalId);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count == 1;
    }

    public boolean updateJournalProperties(int journalId, String propertiesName, String value) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            ps = conn.prepareStatement("SELECT properties from journal WHERE id=?");
            ps.setInt(1, journalId);
            String properties = "";
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                properties = rs.getString("properties");
            }
            PropertyHandler handler = new PropertyHandler();
            handler.setProperties(properties);
            handler.setProperty(propertiesName, value);
            ps = conn.prepareStatement("UPDATE journal set properties=? WHERE id=?");
            ps.setString(1, handler.getProperties());
            ps.setInt(2, journalId);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count == 1;
    }

    public boolean updateJournal(Journal journal) {
        PreparedStatement ps = null;
        int count = 0;
        try {
            ps = conn.prepareStatement("UPDATE journal set parentId=?, position=?, state=?, password=?, title=?, tags=?, content=?, properties=? WHERE id=?");
            ps.setInt(1, journal.getParentId());
            ps.setInt(2, journal.getPosition());
            ps.setInt(3, journal.getState());
            ps.setString(4, journal.getPassword());
            ps.setString(5, journal.getTitle());
            ps.setString(6, journal.getTags());
            ps.setString(7, journal.getContent());
            ps.setString(8, journal.getProperties());
            ps.setInt(9, journal.getId());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return count == 1;
    }

    public int addJournal(Journal journal) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Statement st = null;

        int lastId = 0;
        try {
            ps = conn
                    .prepareStatement("insert into journal (id, parentId, position, state, password, title, tags, content, createDate, properties) values (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, journal.getParentId());
            ps.setInt(2, journal.getPosition());
            ps.setInt(3, journal.getState());
            ps.setString(4, journal.getPassword());
            ps.setString(5, journal.getTitle());
            ps.setString(6, journal.getTags());
            ps.setString(7, journal.getContent());
            ps.setDate(8, new Date(journal.getCreateDate().getTime()));
            ps.setString(9, journal.getProperties());
            ps.executeUpdate();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT last_insert_rowid()");
            rs.next();
            lastId = rs.getInt(1);
            journal.setId(lastId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lastId;
    }

    public void importFromDir(int parentId, final File base) throws IOException {
        if (base.isDirectory()) {
            Journal j = new Journal(parentId, getJournalChildrenCount(parentId), base.getName(), "");
            int id = addJournal(j);
            File[] subFiles = base.listFiles();
            if (subFiles == null)
                return;
            int index = 0;
            for (int i = 0; i < subFiles.length; i++) {
                File f = subFiles[i];
                String name = f.getName();
                if (name.startsWith("."))
                    continue;

                if (f.isDirectory()) {
                    importFromDir(id, f);
                    index++;
                } else {
                    for (String suffix : new String[] { ".txt", ".py", ".xml", ".cpp", ".properties", ".ini", ".html", ".htm", ".c", ".java" }) {
                        if (name.toLowerCase().endsWith(suffix)) {
                            try {
                                System.out.println("import:" + f.getAbsolutePath());
                                j = new Journal(id, index++, FileUtils.getNameWithoutExtension(f.getName()), FileUtils.loadFromFile(f));
                                addJournal(j);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public void export(Journal journal, File base) throws IOException {
        List<Journal> list = getJourlnalList(journal.getId());
        base.mkdirs();
        if (list.size() == 0) {
            if (BlankUtils.isBlank(journal.getContent()))
                return;
            File f = new File(base, journal.getTitle() + ".txt");
            if (f.exists()) {
                f = new File(base, journal.getTitle() + System.currentTimeMillis() + ".txt");
            }
            FileUtils.saveToFile(f, journal.getContent());
        } else {
            File path = new File(base, journal.getTitle());
            path.mkdirs();
            if (!BlankUtils.isBlank(journal.getContent())) {
                File f = new File(path, journal.getTitle() + ".txt");
                FileUtils.saveToFile(f, journal.getContent());
            }
            for (Journal j : list) {
                export(j, path);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        PimDb db = PimDb.getInstance();

        db.open("pim.db");
        db.setup();
        // db.importContacts(new File("/home/xiatian/contacts.xml"));
        // Contact c = new Contact("夏天", "81258792", "13651058369",
        // "iamxiatian@gmail.com", "xiatian", "ruc", "");
        // db.addContactl(c);
        // //for(int i=1; i<50; i++){
        // c = new Contact("jiluyu" , "81258792", "13651058369",
        // "iamxiatian@gmail.com", "family", "ruc", "");
        // db.addContactl(c);
        // //}

        // File f = new
        // File("/home/xiatian/workspace/buge/pim/src/xiatian/pim/component/tree/DragAndDropDropTargetListener.java");
        // Journal j = new Journal(0, 0,
        // FileUtils.getNameWithoutExtension(f.getName()),
        // FileUtils.loadFromFile(f));
        // db.addJournal(j);
        // Journal j = new Journal(0, 0, "日记", "我的日记");
        // db.addJournal(j);
        // j = new Journal(1, 0, "2009", "我的日记");
        // db.addJournal(j);
        // j = new Journal(1, 1, "2010", "我的日记");
        // db.addJournal(j);
        // j = new Journal(1, 2, "2011", "我的日记");
        // db.addJournal(j);
        // for (Journal a : db.getJourlnalList(0)) {
        // System.out.println(a);
        // for (Journal b : db.getJourlnalList(a.getId())) {
        // System.out.println(b);
        // }
        // }
        db.close();
    }

}
