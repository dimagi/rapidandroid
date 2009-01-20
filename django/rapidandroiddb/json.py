from django.core import serializers
from rapidandroid.models import *

def forms():
    all_objects = list(Form.objects.all())
    data = serializers.serialize('json', all_objects)
    return data

def fields():
    all_objects = list(Field.objects.all())
    data = serializers.serialize('json', all_objects)
    return data

def types():
    all_objects = list(FieldType.objects.all())
    data = serializers.serialize('json', all_objects)
    return data

def tofile(filename, data): 
    fout = open(filename,'w')
    fout.write(data)
    fout.close()

def doall(): 
    tofile('forms.json',forms())
    tofile('fields.json',fields())
    tofile('types.json',types())

