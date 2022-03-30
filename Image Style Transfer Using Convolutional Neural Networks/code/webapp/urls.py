
from django.urls import path
from webapp.views import hello
from webapp.views import uplogic
from webapp.views import query

app_name = 'webapp'

urlpatterns = [
    path('uplogic/',uplogic,name='uplogic'),
    path('hello/',hello,name='hello'),
    path('output/',query,name='output'),
]
