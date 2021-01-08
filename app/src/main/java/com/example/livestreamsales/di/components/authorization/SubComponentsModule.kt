package com.example.livestreamsales.di.components.authorization

import com.example.livestreamsales.di.components.telephonenumberinput.TelephoneNumberInputComponent
import dagger.Module

@Module(subcomponents = [
    TelephoneNumberInputComponent::class
])
class SubComponentsModule