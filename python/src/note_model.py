# -*- coding: utf-8 -*-

from PyQt4 import QtCore
from PyQt4 import QtGui
from copy import deepcopy
from db import pimDb
from conf import current_file_dir

class TreeItem(object):
        def __init__(self, data, parent=None):
                self.parentItem = parent
                self.itemData = data
                self.childItems = []
                for row in pimDb.get_notes(data['id']):
                        self.childItems.append(TreeItem(row, self))

        def appendChild(self, item):
                self.childItems.append(item)

        def child(self, row):
                return self.childItems[row]

        def childCount(self):
                return len(self.childItems)

        def columnCount(self):
                #return len(self.itemData)
                return 1

        def note(self):
                return self.itemData

        def title(self):
                try:
                        return self.itemData['title']
                except IndexError:
                        return None

        def parent(self):
                return self.parentItem
        
        def moveLeft(self):
                """
                当前节点上移
                """
                parentItem = self.parent()
                if parentItem ==None:return
                grandpaItem = parentItem.parent()
                if grandpaItem == None: return
                
                currentItem = parentItem.childItems.pop(self.row())
                currentItem.parentItem = grandpaItem
                grandpaItem.childItems.append(currentItem)

                currentNote = currentItem.note()
                currentNote['parentId'] = grandpaItem.note()['id']
                currentNote['position'] = currentItem.row()
                pimDb.update_note(currentNote)

        def moveRight(self):
                """
                当前节点上移
                """
                parentItem = self.parent()
                row_position = self.row()
                if row_position<=0: return
                upperItem = parentItem.childItems[row_position-1]
                currentItem = parentItem.childItems.pop(self.row())
                currentItem.parentItem = upperItem
                upperItem.childItems.append(currentItem)
                currentNote = currentItem.note()
                currentNote['parentId'] = upperItem.note()['id']
                currentNote['position'] = currentItem.row()
                pimDb.update_note(currentNote)

        def moveUpDown(self, direction='up'):
                """
                当前节点上移
                """
                parentItem = self.parent()
                row_position = self.row()
                another_position = -1
                if direction=='up':
                        another_position  = row_position - 1
                else:
                        another_position  = row_position + 1

                if another_position<0 or another_position>=parentItem.childCount():
                        return
                anotherItem = self.parentItem.childItems[another_position]
                tmp = deepcopy(self.itemData)
                self.itemData = deepcopy(anotherItem.itemData)
                anotherItem.itemData = deepcopy(tmp)

                tmp = deepcopy(self.childItems)
                self.childItems = deepcopy(anotherItem.childItems)
                anotherItem.childItems = deepcopy(tmp)
                self.itemData['position'] = self.row()
                anotherItem.itemData['position'] = anotherItem.row()

                pimDb.change_note_position(self.note()['id'], anotherItem.note()['id'], self.itemData['position'], anotherItem.itemData['position'])

        def row(self):
                """
                返回当前节点的行位置
                """
                if self.parentItem:
                        return self.parentItem.childItems.index(self)

                return 0

        def insertChild(self):
                newNote = pimDb.new_note(self.itemData['id'])
                self.childItems.append(TreeItem(newNote, self))
                return True

        def removeChild(self, position):
                item = self.childItems.pop(position)
                pimDb.remove_note(item.note()['id'])
                return True
        
        def setTitle(self, value):
                self.itemData['title'] = value
                pimDb.update_note_title(self.itemData['id'], value)
                return True

class TreeModel(QtCore.QAbstractItemModel):
        def __init__(self, parent=None):
                super(TreeModel, self).__init__(parent)

                self.rootItem = TreeItem({"id":0, "title":"Note"})
                #self.setupModelData(self.rootItem)

        def columnCount(self, parent):
                if parent.isValid():
                        return parent.internalPointer().columnCount()
                else:
                        return self.rootItem.columnCount()

        def data(self, index, role=QtCore.Qt.DisplayRole):
                """
                显示时调用该方法
                """
                if not index.isValid():
                        return None

                if role == QtCore.Qt.DecorationRole:
                        if self.rowCount(index)>0:
                                return QtGui.QIcon( current_file_dir() + "/icons/folder.png")
                        else:
                                return QtGui.QIcon( current_file_dir() + "/icons/leaf.png")
                
                if role != QtCore.Qt.DisplayRole and role!=QtCore.Qt.EditRole:
                        return None

                item = index.internalPointer()

                return item.title()

        def getItem(self, index):
                if index.isValid():
                        item = index.internalPointer()
                        if item:
                                return item

                return self.rootItem

        def setData(self, index, value, role=QtCore.Qt.EditRole):
                """
                把用户输入的字符串设置为note的新标题
                """
                if role != QtCore.Qt.EditRole:
                        return False

                item = self.getItem(index)
                result = item.setTitle(unicode(value.toString()))

                if result:
                        self.dataChanged.emit(index, index)

                return result

        def insertRow(self, position, parent=QtCore.QModelIndex()):
                parentItem = self.getItem(parent)
                self.beginInsertRows(parent, position, position)
                success = parentItem.insertChild()
                self.endInsertRows()

                return success

        def removeRow(self, position, parent=QtCore.QModelIndex()):
                parentItem = self.getItem(parent)

                self.beginRemoveRows(parent, position, position)
                success = parentItem.removeChild(position)
                self.endRemoveRows()

                return success

        def flags(self, index):
                if not index.isValid():
                        return QtCore.Qt.NoItemFlags

                return QtCore.Qt.ItemIsEnabled | QtCore.Qt.ItemIsSelectable | QtCore.Qt.ItemIsEditable

        def headerData(self, section, orientation, role):
                if orientation == QtCore.Qt.Horizontal and role == QtCore.Qt.DisplayRole:
                        return self.rootItem.data()

                return None

        def index(self, row, column, parent):
                """
                通过该方法设置该位置的信息
                """
                if not self.hasIndex(row, column, parent):
                        return QtCore.QModelIndex()

                if not parent.isValid():
                        parentItem = self.rootItem
                else:
                        parentItem = parent.internalPointer()

                childItem = parentItem.child(row)
                if childItem:
                        return self.createIndex(row, column, childItem)
                else:
                        return QtCore.QModelIndex()

        def parent(self, index):
                if not index.isValid():
                        return QtCore.QModelIndex()

                childItem = index.internalPointer()
                parentItem = childItem.parent()

                if parentItem == self.rootItem or parentItem == None:
                        return QtCore.QModelIndex()

                return self.createIndex(parentItem.row(), 0, parentItem)

        def rowCount(self, parent):
                if parent.column() > 0:
                        return 0

                if not parent.isValid():
                        parentItem = self.rootItem
                else:
                        parentItem = parent.internalPointer()

                return parentItem.childCount()

        def moveUpDown(self, index, direction = 'up'):
                item = self.getItem(index)                
                if item == self.rootItem:
                        return
                
                self.layoutAboutToBeChanged.emit()
                item.moveUpDown(direction)
                self.layoutChanged.emit()

        def moveLeft(self, index):
                item = self.getItem(index)
                if item == self.rootItem:
                        return

                self.layoutAboutToBeChanged.emit()
                item.moveLeft()
                self.layoutChanged.emit()

        def moveRight(self, index):
                item = self.getItem(index)
                if item == self.rootItem:
                        return

                self.layoutAboutToBeChanged.emit()
                item.moveRight()
                self.layoutChanged.emit()
