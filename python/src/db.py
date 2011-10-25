import os.path
# -*- coding: utf-8 -*-

from sqlite3 import *
import datetime
import os

class PimDb:
        def __init__(self):
                self.conn = None

        def open(self, dbname):
                if self.conn !=None:
                        self.close()

                self.dbname = dbname
                self.conn = connect(dbname)
                self.conn.row_factory = Row
                self.create_table()

        def create_table(self):
                c = self.conn.cursor()
                c.execute("create table IF NOT EXISTS journal (id INTEGER PRIMARY KEY, parentId INTEGER, position INTEGER,state INTEGER, password TEXT, title TEXT, tags TEXT, content TEXT, createDate DATE)")
                c.execute("CREATE TABLE IF NOT EXISTS contact(id INTEGER PRIMARY KEY,  name varchar(20), position INTEGER,state INTEGER, telephone TEXT, cellphone TEXT, email TEXT, category varchar(20), organization TEXT, addTime datetime, memo text)")
                c.execute("CREATE TABLE IF NOT EXISTS todolist(id INTEGER PRIMARY KEY,  name varchar(20), category varchar(20),addTime DATETIME, startTime DATETIME, endTime DATETIME, finishTime DATETIME INTEGER,state INTEGER, memo text)")
                c.close()
                self.conn.commit()

        def quotesql(text):
                return text.replace("\\", "\\\\").replace("'", "''")

        def get_note(self, id):
                """
                get one note by given id
                """
                c = self.conn.cursor()
                c.execute("select * from journal where id = %d" % id)
                record = c.fetchone()
                c.close()
                if(record == None):
                        return None
                else:
                        values = {}
                        for key in record.keys():
                                values[key] = record[key]
                        return values

        def get_note_content(self, id):
                c = self.conn.cursor()
                content = c.execute("select content from journal where id = %d" % id).fetchone()[0]
                c.close()
                return content

        def new_note(self, parentId):
                position = self.get_children_count(parentId) + 1
                c = self.conn.cursor()
                title = datetime.datetime.now().date().isoformat()
                params = (parentId, position, 0, '', title, '', '', datetime.date.today())
                c.execute("insert into journal (id, parentId, position, state, password, title, tags, content, createDate) values (NULL, ?, ?, ?, ?, ?, ?, ?, ?)" , params)
                id = c.execute("SELECT last_insert_rowid()").fetchone()[0]
                c.close()
                self.conn.commit()
                
                return self. get_note(id)

        def change_note_position(self, id1, id2, position1, position2):
                """
                把id1,id2的位置分别设为position1, position2
                """
                c = self.conn.cursor()
                c.execute("update journal set position = ? where id = ?", (position1, id1))
                c.execute("update journal set position = ? where id = ?", (position2, id2))
                c.close()
                self.conn.commit()

        def remove_note(self, id):
                c = self.conn.cursor()
                c.execute("delete from journal where id=%d" % id)
                c.close()
                self.conn.commit()

        def update_note_content(self, id, content):
                c = self.conn.cursor()
                c.execute("update journal set content =? where id = ?",  (content, id))
                c.close()
                self.conn.commit()

        def update_note_title(self, id, title):
                c = self.conn.cursor()
                c.execute("update journal set title =? where id = ?",  (title, id))
                c.close()
                self.conn.commit()

        def update_note(self, note):
                c = self.conn.cursor()
                c.execute("update journal set title =?, parentId=?, position=? where id = ?",  (note['title'], note['parentId'], note['position'], note['id']))
                c.close()
                self.conn.commit()

        def get_notes(self, parentId):
                """
                get all notes by given parentId
                """
                c = self.conn.cursor()
                c.execute("select id, title, parentId, state, position, password, createDate from journal where parentId = %d  order by position" % parentId)
                all = c.fetchall()
                c.close()
                values = []
                for one in all:
                        v = {}
                        for key in one.keys():
                                v[key] = one[key]
                        values.append(v)
                        
                return values

        def get_children_count(self, parentId):
                """
                get children count
                """
                c = self.conn.cursor()
                count = c.execute("select count(*) from journal where parentId = %d" % parentId).fetchone()[0]
                c.close()
                return count

        def close(self):
                if self.conn != None:
                        self.conn.commit()
                        self.conn.close()
                        self.conn = None
         
pimDb = PimDb()
#pimDb.open('/home/xiatian/test.pim')
