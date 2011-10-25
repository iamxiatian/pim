#! /usr/bin/python

# -*- coding: utf-8 -*-

import sys, os
from PyQt4.QtGui import QFont
from PyQt4.QtGui import QColor

class PimConf:
        """
        configuration for pim
        """
        def __init__(self):
                self.__confItems = {}
                self.__confFile = "pim.conf"
                self.__read()

        def save(self):
                file = open(self.__confFile, 'w')
                for key in self.__confItems.keys():
                        line = '%s=%s\n' % (key, self.__confItems[key])
                        file.write(line)
                file.close()

        def get(self, key):
                return self.__confItems[key]

        def set(self, key, value):
                self.__confItems[key] = value

        def get_list(self, key):
                list = []
                try:
                        value = self.__confItems[key]
                        list = value.split(';')
                except:
                        pass
                return list

        def set_list(self, key, values):
                s = ''
                for v in values:
                        if len(s)>0:
                                s = s + ';' + v
                        else:
                                s = v

                self.set(key, s)
                
        def __read(self):
                try:
                        for line in open(self.__confFile, 'r').readlines():
                                pos = line.find('=')
                                if pos>0:
                                        key = line[0:pos].strip()
                                        value = line[pos+1:].strip()
                                        self.__confItems[key] = value
                except:
                        print self.__confFile, 'does not exist.'
                        pass

        def get_last_file(self):
                list = self.get_list('last_files')
                if len(list)>0:
                        return list[0]
                else:
                        return None

        def clear_last_files(self):
                try:
                        self.__confItems.pop('last_files')
                except:
                        pass

        def get_last_files(self):
                return self.get_list('last_files')

        def add_last_file(self, filename):
                list = self.get_list('last_files')
                try:
                        index = list.index(filename)
                        list.pop(index)
                except:
                        pass
                list.insert(0, filename)
                print 'add', list
                self.set_list('last_files', list)

        def save_font(self, key, font):
                self.set(key + "_family", font.family())
                self.set(key + "_size", font.pointSize())
                self.set(key + "_style", font.style())

        def get_font(self, key):
                font = QFont()
                try:
                        font = QFont(self.get(key + "_family"), int(self.get(key + '_size')))
                        font.setStyle(int(self.get(key + '_style')))
                except Exception as inst:                        
                        print inst
                return font

        def save_color(self, key, color):
                self.set(key + "_red", color.red())
                self.set(key + "_green", color.green())
                self.set(key + "_blue", color.blue())
                self.set(key + "_alpha", color.alpha())

        def get_color(self, key):
                color = QColor(0,0,0)
                try:
                        color = QColor(int(self.get(key+'_red')), int(self.get(key+'_green')), int(self.get(key+'_blue')), int(self.get(key+'_alpha')))
                except Exception as e:
                        print e
                return color

def current_file_dir():    
        path = sys.path[0]    
        if os.path.isdir(path):
                return path
        elif os.path.isfile(path):
                return os.path.dirname(path)

if __name__=='__main__':
        conf = PimConf()
        conf.set('font', 'Microsoft YaHei')
        conf.set('color', 'red')
        conf.save()
        print conf.get('font')
        print conf.get('color')
