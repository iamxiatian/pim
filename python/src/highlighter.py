'''
Python Syntax Highlighting Example

Copyright (C) 2009 Carson J. Q. Farmer

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public Licence as published by the Free Software
Foundation; either version 2 of the Licence, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE.  See the GNU General Public Licence for more
details.

You should have received a copy of the GNU General Public Licence along with
this program; if not, write to the Free Software Foundation, Inc., 51 Franklin
Street, Fifth Floor, Boston, MA  02110-1301, USA
'''

import sys

from PyQt4.QtCore import *
from PyQt4.QtGui import *

class MyHighlighter(QSyntaxHighlighter):

        def __init__(self, parent, theme):
                QSyntaxHighlighter.__init__(self, parent)
                self.parent = parent
                keyword = QTextCharFormat()
                reservedClasses = QTextCharFormat()
                assignmentOperator = QTextCharFormat()
                delimiter = QTextCharFormat()
                specialConstant = QTextCharFormat()
                boolean = QTextCharFormat()
                colorFormat = QTextCharFormat()
                comment = QTextCharFormat()
                string = QTextCharFormat()
                singleQuotedString = QTextCharFormat()

                self.highlightingRules = []

                # keyword
                brush = QBrush(Qt.darkBlue, Qt.SolidPattern)
                keyword.setForeground(brush)
                keyword.setFontWeight(QFont.Bold)
                keywords = QStringList(["break", "else", "for", "if", "in",
                                       "next", "repeat", "return", "switch",
                                       "try", "while"])
                for word in keywords:
                        pattern = QRegExp("\\b" + word + "\\b")
                        rule = HighlightingRule(pattern, keyword)
                        self.highlightingRules.append(rule)

                # reservedClasses
                reservedClasses.setForeground(brush)
                reservedClasses.setFontWeight(QFont.Bold)
                keywords = QStringList(["array", "character", "complex",
                                       "data.frame", "double", "factor",
                                       "function", "integer", "list",
                                       "logical", "matrix", "numeric",
                                       "vector"])
                for word in keywords:
                        pattern = QRegExp("\\b" + word + "\\b")
                        rule = HighlightingRule(pattern, reservedClasses)
                        self.highlightingRules.append(rule)


                # assignmentOperator
                brush = QBrush(Qt.darkBlue, Qt.SolidPattern)
                pattern = QRegExp("(<){1,2}-")
                assignmentOperator.setForeground(brush)
                assignmentOperator.setFontWeight(QFont.Bold)
                rule = HighlightingRule(pattern, assignmentOperator)
                self.highlightingRules.append(rule)

                # delimiter
                pattern = QRegExp("[\)\(]+|[\{\}]+|[][]+")
                delimiter.setForeground(brush)
                delimiter.setFontWeight(QFont.Bold)
                rule = HighlightingRule(pattern, delimiter)
                self.highlightingRules.append(rule)

                # specialConstant
                brush = QBrush(Qt.green, Qt.SolidPattern)
                specialConstant.setForeground(brush)
                keywords = QStringList(["Inf", "NA", "NaN", "NULL"])
                for word in keywords:
                        pattern = QRegExp("\\b" + word + "\\b")
                        rule = HighlightingRule(pattern, specialConstant)
                        self.highlightingRules.append(rule)

                # boolean
                boolean.setForeground(brush)
                keywords = QStringList(["TRUE", "FALSE"])
                for word in keywords:
                        pattern = QRegExp("\\b" + word + "\\b")
                        rule = HighlightingRule(pattern, boolean)
                        self.highlightingRules.append(rule)

                # red
                brush = QBrush(Qt.red, Qt.SolidPattern)
                pattern = QRegExp("<red>.*\</red>")
                pattern.setMinimal(True)
                colorFormat.setForeground(brush)
                rule = HighlightingRule(pattern, colorFormat)
                self.highlightingRules.append(rule)
                
                # comment
                brush = QBrush(Qt.blue, Qt.SolidPattern)
                pattern = QRegExp("#[^\n]*")
                comment.setForeground(brush)
                rule = HighlightingRule(pattern, comment)
                self.highlightingRules.append(rule)

                format = QTextCharFormat()
                brush = QBrush(Qt.gray, Qt.SolidPattern)
                pattern = QRegExp("DATE:[^\n]*")
                format.setForeground(brush)
                rule = HighlightingRule(pattern, format)
                self.highlightingRules.append(rule)

                format = QTextCharFormat()
                brush = QBrush(Qt.red, Qt.SolidPattern)
                pattern = QRegExp("TITLE:[^\n]*")
                format.setFontWeight(QFont.Bold)
                format.setFontUnderline(True)
                format.setForeground(brush)
                rule = HighlightingRule(pattern, format)
                self.highlightingRules.append(rule)

                format = QTextCharFormat()
		brush = QBrush(Qt.blue, Qt.SolidPattern)
                pattern = QRegExp("http://[\w|\.|/|_|\-|%]*")
                format.setFontItalic(True)
                format.setFontUnderline(True)
                format.setForeground(brush)
                rule = HighlightingRule(pattern, format)
                self.highlightingRules.append(rule)

                format = QTextCharFormat()
                brush = QBrush(Qt.red, Qt.SolidPattern)
                pattern = QRegExp("\[RED\][^\n]*")
                format.setForeground(brush)
                rule = HighlightingRule(pattern, format)
                self.highlightingRules.append(rule)

                # string
                brush = QBrush(Qt.red, Qt.SolidPattern)
                pattern = QRegExp("\".*\"")
                pattern.setMinimal(True)
                string.setForeground(brush)
                rule = HighlightingRule(pattern, string)
                self.highlightingRules.append(rule)

                # singleQuotedString
                pattern = QRegExp("\'.*\'")
                pattern.setMinimal(True)
                singleQuotedString.setForeground(brush)
                rule = HighlightingRule(pattern, singleQuotedString)
                self.highlightingRules.append(rule)

        def highlightBlock(self, text):
                for rule in self.highlightingRules:
                        expression = QRegExp(rule.pattern)
                        index = expression.indexIn(text)
                        while index >= 0:
                                length = expression.matchedLength()
                                self.setFormat(index, length, rule.format)
                                index = text.indexOf(expression, index + length)
                self.setCurrentBlockState(0)

class HighlightingRule():
        def __init__(self, pattern, format):
                self.pattern = pattern
                self.format = format

class TestApp(QMainWindow):
        def __init__(self):
                QMainWindow.__init__(self)
                font = QFont()
                font.setFamily("Courier")
                font.setFixedPitch(True)
                font.setPointSize(10)
                editor = QTextEdit()
                editor.setFont(font)
                highlighter = MyHighlighter(editor, "Classic")
                self.setCentralWidget(editor)
                self.setWindowTitle("Syntax Highlighter")


if __name__ == "__main__":
        app = QApplication(sys.argv)
        window = TestApp()
        window.show()
        sys.exit(app.exec_())
