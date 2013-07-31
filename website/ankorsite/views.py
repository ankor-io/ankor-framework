from django.http import HttpResponse
from django.template import RequestContext, loader

def index(request):
    template = loader.get_template('index.html')
    context = RequestContext(request, {
                'activeMenu': 'home',
        })
    return HttpResponse(template.render(context))

def download(request):
    template = loader.get_template('download.html')
    context = RequestContext(request, {
                'activeMenu': 'download',
        })
    return HttpResponse(template.render(context))

def tutorials(request, type, step):
    template = loader.get_template('tutorial/tutorial_' + type + '_' + step + '.html')

    step = int(step)

    nextStep = int(step) + 1
    if (nextStep > 2):
        nextStep = 2
    previousStep = int(step) - 1
    if (previousStep < 0):
        previousStep = 0

    context = RequestContext(request, {
                'activeMenu' : 'tutorials',
                'step' : step,
                'previousStep' : previousStep,
                'nextStep' : nextStep,
        })
    return HttpResponse(template.render(context))

def documentation(request):
    template = loader.get_template('documentation.html')
    context = RequestContext(request, {
                'activeMenu': 'documentation',
        })
    return HttpResponse(template.render(context))

def contribute(request):
    template = loader.get_template('contribute.html')
    context = RequestContext(request, {
                'activeMenu': 'contribute',
        })
    return HttpResponse(template.render(context))
