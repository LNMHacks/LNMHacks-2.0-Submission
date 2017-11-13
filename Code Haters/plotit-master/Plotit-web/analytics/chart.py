from bokeh.charts import Bar, Scatter, BoxPlot, Histogram, output_file, show
from bokeh.sampledata.autompg import autompg as df
from bokeh.plotting import figure, output_file, show
from bokeh.resources import CDN
from bokeh.embed import components



# prepare some data, a Pandas GroupBy object in this case
def create_chart(df,xaxis,yaxis,graphtype):
    if graphtype=="Line Graph":
        plot = lineplot(df,xaxis,yaxis,xaxis+" vs "+yaxis)
    elif graphtype=="Scatter Plot":
        plot = scatter(df,xaxis,yaxis,xaxis+" vs "+yaxis)
    elif graphtype == "Bar Chat":
        plot = Bar(df,xaxis,yaxis,xaxis+" vs "+yaxis)
    else:
        pass
    return plot


# create a scatter chart
def lineplot(df,x_axis,y_axis,title):
    plot = figure(title= title , x_axis_label=x_axis , y_axis_label= y_axis, plot_width =400, plot_height =400)
    plot.line(df[x_axis], df[y_axis], line_width = 2)
    return plot

def scatter(df,x_axis,y_axis,title):
    plot = Scatter(df, x=x_axis, y=y_axis, color=x_axis,
            title=title,
            legend='top_right',
            xlabel=x_axis,
            ylabel=y_axis)
    return plot

def Bar(df,x_axis,y_axis,title):
    plot = Bar(df, label=x_axis, values=y_axis, title=title, color=x_axis, xlabel= x_axis,
            ylabel= y_axis, plot_width =width, plot_height =height)
#    script, div = components(plot)
    return plot

def BoxPlot(df,x_axis,y_axis,title):
    plot = BoxPlot(df, values=y_axis, label=x_axis, title=title, color=x_axis, xlabel= x_label, outliers=True,
            ylabel= y_axis, plot_width =width, plot_height =height,  whisker_color=x_axis)
#    script, div = components(plot)
    return plot

def Histograms():
    plot = Histogram(df, values= value, color=value,
              title=title, legend='top_right')
#    script, div = components(plot)
    return plot


#plot = figure(title= title , xlabel= 'X-Axis', ylabel= 'Y- Axis', plot_width =400, plot_height =400)
#plot.line(domain, y, legend= 'f(x)', line_width = 2)
