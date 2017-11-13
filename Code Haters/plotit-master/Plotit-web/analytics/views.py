from django.shortcuts import render, get_object_or_404, redirect, render_to_response, HttpResponseRedirect
from django.core.urlresolvers import reverse
from bokeh.plotting import figure, output_file, show
from bokeh.resources import CDN
from bokeh.embed import components
import math
from django.http import JsonResponse
from django.conf import settings
from bokeh.charts import Scatter, output_file, show
#from bokeh.sampledata.autompg import autompg as df
from .chart import *
from .models import Data
from .forms import DataForm
import pandas as pd

# Create your views here.


def file_upload(request):
    if request.method == 'POST':
        data_form = DataForm(request.POST, request.FILES)
        #print(data_form)
        if data_form.is_valid():
            print("valid")
            datasave = data_form.save(commit= False)
            if 'file_name' in request.FILES:
                data_form.file_name = request.FILES['file_name']
            data_form.save()
            print (settings.MEDIA_ROOT+"Files/"+str(data_form.file_name))
            filepath = str(settings.MEDIA_ROOT+"/Files/"+str(data_form.file_name))
            request.session['filepath'] = filepath
            return redirect('analytics:index')# TODO: redirect url

        else:
            print(data_form.errors)
            return render(request, 'analytics/error.html')

    else:
        data_form = DataForm()
        return render(request, 'analytics/file_upload.html',{'data_form':data_form})

def index(request):
    if not request.session['filepath']:
        return HttpResponseRedirect(reverse('file_upload'))
    else:

        filepath = request.session['filepath']
        df = pd.read_csv(filepath)
        columns = df.columns
        data = df.head()
        data = data.to_html(classes=["table","table-bordered", "table-striped", "table-hover"])
        if request.method == "GET" :

            return render_to_response('analytics/index.html',{'columns':columns, 'data':data})

        elif request.method == "POST" :
            xaxis  = request.POST['xaxis']
            yaxis  = request.POST['yaxis']
            graphtype = request.POST['graphtype']
            if xaxis == yaxis:
                message = "X - Axis and Y - Axis must not be same"
                return render_to_response( 'analytics/index.html' ,{'columns':columns, 'data':data, 'message':message})
                print(xaxis,yaxis)
                plot = create_chart(df,xaxis,yaxis,graphtype)
        #plot = Scatter(df, x=xaxis, y=yaxis,color=xaxis , title="HP vs MPG",xlabel="Miles Per Gallon", ylabel="Horsepower")
            script, div = components(plot)
            return graph_display(request,plot)



        else:
            pass



def graph_display(request,plot):
    #script = request.session['script']
    #div = request.session['div']
    #plot = request.session['plot'][0]
    #graphtype = request.session['graphtype']
    #yaxis = request.session['yaxis']
    #xaxis = request.session['xaxis']
    #df = request.session['df']
    #plot = Scatter(df, x=xaxis, y=yaxis,color=xaxis , title="HP vs MPG",xlabel="Miles Per Gallon", ylabel="Horsepower")
    script, div = components(plot)

    return render(request, 'analytics/graph.html',{ 'script' : script , 'div' : div})
