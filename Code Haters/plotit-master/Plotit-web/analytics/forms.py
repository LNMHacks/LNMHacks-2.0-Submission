from django import forms
from .models import Data


class DataForm(forms.ModelForm):
    file_name = forms.FileField()
    class Meta:
        model = Data
        fields = ('file_name',)
