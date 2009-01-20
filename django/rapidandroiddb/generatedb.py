from django.db import connection
from rapidandroid.models import *
import string

#simple script for generating the db in sqlite
# and capturing the create_table statements from the debugger and outputting them as an array.
# this was used for transferring the create table statements to android for the sqlite table creation

#though, some more munging needed to be done.  Namely, escape out the double quotes, and change the id columns to _id

def run():
    from django.core.management import call_command 
    call_command('syncdb')
    creates = []
    for sqlhash in connection.queries:
        if string.count(sqlhash['sql'],"CREATE TABLE") == 1:
            creates.append(sqlhash['sql'])
    return creates
        



