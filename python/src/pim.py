#!/usr/bin/python

# -*- coding: utf-8 -*-

import sys

from PyQt4 import Qt
from PyQt4 import QtCore
from PyQt4 import QtGui

from highlighter import MyHighlighter
from note_model import TreeModel
from ui_main import Ui_MainWindow

from db import pimDb
from conf import PimConf
from conf import current_file_dir

class PimAction(Qt.QAction):
        """
        extended QActin to include more information
        """
        def __init__(self, icon, name, parent = None):
                Qt.QAction.__init__(self, icon, name,  parent)
                self.menuText = name
        
        def getMenuText(self):
                return self.menuText

class MainWindow(QtGui.QMainWindow, Ui_MainWindow):
        def __init__(self, parent=None):
                QtGui.QMainWindow.__init__(self, parent)
                self.setupUi(self)
                self.setWindowIcon(QtGui.QIcon(current_file_dir() + '/icons/icon.png'))

                self.pimConf = PimConf()
                self.currentNoteId = 0

                #self.ui = Ui_MainWindow()
                #self.ui.setupUi(self)

                self.textNote.setFont(self.pimConf.get_font('note_font'))
                self.textNote.setTextColor(self.pimConf.get_color('note_color'))
                self.db_file = self.pimConf.get_last_file()
                
                if self.db_file!=None:
                        pimDb.open(self.db_file)
                        self.setWindowTitle('PIM4XIATIAN - %s' % self.db_file)
                        self.treeNote.setModel(TreeModel())

                self.splitter.setSizes([120, 400])
                self.createNoteContextMenus()

                highlighter = MyHighlighter(self.textNote, "Classic")

                self.center()
                self.showMaximized()
                
                #add signals and slots
                #self.connect(self.ui.treeNote, QtCore.SIGNAL("clicked(QModelIndex)"), self.changeText())
                QtCore.QObject.connect(self.treeNote,QtCore.SIGNAL("clicked(QModelIndex)"), self.changeText)
                QtCore.QObject.connect(self.treeNote,QtCore.SIGNAL("clicked(QModelIndex)"), self.updateActions)
                QtCore.QObject.connect(self.actionSave, QtCore.SIGNAL("triggered()"), self.saveNote)

                self.actionEditorFont.triggered.connect(self.change_note_font)
                self.actionEditorColor.triggered.connect(self.change_note_color)
                
                self.actionOpen.triggered.connect(self.openDb)
                for f in self.pimConf.get_last_files():
                        if f==None or f=='': continue
                        action = PimAction(QtGui.QIcon("icons/icon.png"),f, self)
                        self.menuOpenRecent.addAction(action)
                        action.triggered.connect(self.openRecent)
                self.menuOpenRecent.addSeparator()
                action = Qt.QAction(QtGui.QIcon("icons/icon.png"), "&Clear recent files", self)
                self.menuOpenRecent.addAction(action)
                action.triggered.connect(self.clearRecent)

        def createNoteContextMenus(self):
                self.appendChildAction = Qt.QAction(QtGui.QIcon("icons/icon.png"), "&New Child Note", self)
                self.appendChildAction.setShortcut("F3")
                self.treeNote.addAction(self.appendChildAction)
                self.appendChildAction.triggered.connect(self.insertChildNote)

                self.removeNoteAction = Qt.QAction(QtGui.QIcon("icons/icon.png"), "&Remove", self)
                self.treeNote.addAction(self.removeNoteAction)
                self.removeNoteAction.triggered.connect(self.removeNote)

                self.upNoteAction = Qt.QAction(QtGui.QIcon("icons/icon.png"), "Move &Up", self)
                self.upNoteAction.setShortcut("Ctrl+Up")
                self.treeNote.addAction(self.upNoteAction)
                self.upNoteAction.triggered.connect(self.moveUpNote)

                self.downNoteAction = Qt.QAction(QtGui.QIcon("icons/icon.png"), "Move &Down", self)
                self.downNoteAction.setShortcut("Ctrl+Down")
                self.treeNote.addAction(self.downNoteAction)
                self.downNoteAction.triggered.connect(self.moveDownNote)

                self.leftNoteAction = Qt.QAction(QtGui.QIcon("icons/icon.png"), "Move &Left", self)
                self.leftNoteAction.setShortcut("Ctrl+Left")
                self.treeNote.addAction(self.leftNoteAction)
                self.leftNoteAction.triggered.connect(self.moveLeftNote)

                self.rightNoteAction = Qt.QAction(QtGui.QIcon("icons/icon.png"), "Move &Right", self)
                self.rightNoteAction.setShortcut("Ctrl+Right")
                self.treeNote.addAction(self.rightNoteAction)
                self.rightNoteAction.triggered.connect(self.moveRightNote)

                #QtCore.QObject.connect(self.appendChildAction, QtCore.SIGNAL("triggered()"), self.newNote)

        def clearRecent(self):
                self.pimConf.clear_last_files()
                self.menuOpenRecent.clear()
                
        def openRecent(self):
                action = self.sender()                
                f = action.getMenuText()
                pimDb.open(f)
                self.currentNoteId = 0
                self.treeNote.setModel(TreeModel())
                self.setWindowTitle('PIM4XIATIAN - %s' % f)
                self.db_file = f
                self.pimConf.add_last_file(self.db_file)
                
        def openDb(self):
                filename = QtGui.QFileDialog.getOpenFileName(self, 'Open or Create Pim File')
                if not filename.isEmpty():
                        pimDb.open(unicode(filename))
                        self.currentNoteId = 0
                        self.treeNote.setModel(TreeModel())
                        self.setWindowTitle('PIM4XIATIAN - %s' % unicode(filename))
                        self.db_file = '%s' % unicode(filename)
                        self.pimConf.add_last_file(self.db_file)

        def insertChildNote(self):
                index = self.treeNote.selectionModel().currentIndex()
                model = self.treeNote.model()

                if model.columnCount(index) == 0:
                        if not model.insertColumn(0, index):
                                return

                position = model.rowCount(index)
                if not model.insertRow(position,index):
                        return

                self.treeNote.selectionModel().setCurrentIndex(model.index(position, 0, index),
                                                           QtGui.QItemSelectionModel.ClearAndSelect)                

        def removeNote(self):
                index = self.treeNote.selectionModel().currentIndex()
                model = self.treeNote.model()
                if model.rowCount(index)>0:
                        QtGui.QMessageBox.information(self, 'Warning', 'Only leaf note can be deleted!')
                else:
                        reply = QtGui.QMessageBox.question(self, 'Message', "Are you sure to delete current item?", QtGui.QMessageBox.No, QtGui.QMessageBox.Yes)
                        if reply == QtGui.QMessageBox.Yes:
                                model.removeRow(index.row(), index.parent())
                        
	def changeText(self, index):
                """
                slot for text chagned
                """
                if index.internalPointer()==None: return

                note = index.internalPointer().note()

                #if(self.currentNoteId!=note['id']):
                #       self.saveNote()
                self.currentNoteId = note['id']
                content = pimDb.get_note_content(note['id'])
                self.textNote.setText(content)

        def moveUpNote(self):
                index = self.treeNote.selectionModel().currentIndex()                
                model = self.treeNote.model()
                model.moveUpDown(index, 'up')
                self.treeNote.selectionModel().select(index)

        def moveDownNote(self):
                index = self.treeNote.selectionModel().currentIndex()
                model = self.treeNote.model()
                model.moveUpDown(index, 'down')
                self.changeText(index)

        def moveLeftNote(self):
                index = self.treeNote.selectionModel().currentIndex()
                model = self.treeNote.model()
                model.moveLeft(index)
                self.changeText(index)

        def moveRightNote(self):
                index = self.treeNote.selectionModel().currentIndex()
                model = self.treeNote.model()
                model.moveRight(index)
                self.changeText(index)

        def saveNote(self):
                """
                slot for save
                """
                if(self.currentNoteId!=0):
                        text = unicode(self.textNote.toPlainText())
                        pimDb.update_note_content(self.currentNoteId, text)
                        self.statusBar().showMessage("Item: content has be saved!")
                #list = self.ui.treeNote.selectionModel().selectedIndexes()
                #selectionModel = QtGui.QItemSelectionModel(self.treeNoteModel)
                #list = selectionModel.selectedIndexes()
               
                #print self.treeNoteModel.data(index).encode('utf8')
        def change_note_font(self):
                (font, ok) = QtGui.QFontDialog.getFont(self.textNote.textCursor().charFormat().font(), self, 'choose font')
                if ok:
                        self.textNote.setFont(font)
                        self.pimConf.save_font('note_font', font)

        def change_note_color(self):                
                color = QtGui.QColorDialog.getColor(self.textNote.textColor(), self, 'choose color')
                if color.isValid():
                        self.textNote.setTextColor(color)
                        self.pimConf.save_color('note_color', color)

        def center(self):
                screen = QtGui.QDesktopWidget().screenGeometry()
                size = self.geometry()
                self.move((screen.width()-size.width()) / 2, (screen.height()-size.height()) / 2)

        def closeEvent(self, event):
                reply = QtGui.QMessageBox.question(self, 'Message', "Are you sure to quit?", QtGui.QMessageBox.No, QtGui.QMessageBox.Yes)
                if reply == QtGui.QMessageBox.Yes:
                        pimDb.close()                       
                        self.pimConf.save()
                        event.accept()                        
                else:
                        event.ignore()

        def updateActions(self):
                hasSelection = not self.treeNote.selectionModel().selection().isEmpty()
                self.removeNoteAction.setEnabled(hasSelection)
                self.appendChildAction.setEnabled(hasSelection)

                hasCurrent = self.treeNote.selectionModel().currentIndex().isValid()
                
                if hasCurrent:
                        index = self.treeNote.selectionModel().currentIndex()
                        model = self.treeNote.model()
                        self.statusBar().showMessage("Item: %s" % model.data(index))
                        
app = QtGui.QApplication(sys.argv)
window = MainWindow()
window.show()
sys.exit(app.exec_())

