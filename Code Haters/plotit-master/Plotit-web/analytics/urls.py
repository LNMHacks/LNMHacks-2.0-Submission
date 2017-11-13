from django.conf.urls import url
from . import views

app_name = 'analytics'

urlpatterns = [
        url(r'^file_upload$', views.file_upload, name='file_upload'),
        url(r'^graph_display/(?P<plot>[-\w]+)/$', views.graph_display, name='graph_display'),
        url(r'^index$', views.index, name='index'),
    ]
