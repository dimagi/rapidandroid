from django.contrib import admin
from django.contrib.auth import admin as auth_admin
from models import *


class FieldInline(admin.StackedInline):
    model = Field    

class FormAdmin(admin.ModelAdmin):
    list_display = ('id','formname', 'prefix', 'description', 'parsemethod')
    inlines = [FieldInline]


class FieldAdmin(admin.ModelAdmin):
    list_display = ('id','form', 'sequence', 'name', 'prompt','fieldtype')
    search_fields = ('form', 'name', 'fieldtype','prompt')

   

class FieldTypeAdmin(admin.ModelAdmin):
    list_display = ('id','name', 'datatype', 'regex', )
    search_fields = ('name', 'datatype', 'regex')

admin.site.register(Form, FormAdmin)
admin.site.register(Field, FieldAdmin)
admin.site.register(FieldType, FieldTypeAdmin)
