#Name: Adam Lieu
#Student ID: 100451790
#Description:
#Part 2A of Assignment 1, simulate the spread of an infectious disease

# Constant Spread Rate: 0.15
# Fatality rate: 0.025
# Recovery rate: 0.15

import itertools as it
import numpy as np
import scipy as sp
import pylab as pl
import matplotlib.pyplot as plt
import random

def simulateDay(numContacts, numInfected, numPeople):
    #For each susceptible person (not infected, dead, or recovered)
    #infectionRate = spreadProb * numContacts * numInfected / numPeople
    #For each infected person:
    #deathProb should be something between 1 and 0
    #Return:
    # - Number of fatalities
    # - Number of infected people
    # - Number of Recovered people
    # - Number of Susceptible people

    #numContacts - Times a typical person come into contact with people
    #numPeople - total population

    spreadProb = 0.15
    deathProb = 0.025
    recoverProb = 0.15

    numRecovered = 0
    numDeath = 0

    #Simulate if the susceptible person becomes infected
    infectionRate = (spreadProb * numContacts * numInfected) / numPeople
    print infectionRate

    #Number of susceptible people
    numOfSusPeople = numPeople - numInfected
    #rand = random.random()

    #Simulate if suspectible person gets infected or not

    for i in range(numOfSusPeople):
        rand = random.random()
        if rand < infectionRate:
            numInfected += 1
            numOfSusPeople -= 1



    #Simulate if infected person recovers or dies
    for i in range(numInfected):
        rand = random.random()
        if rand < deathProb:
            # patient dies
            numPeople -= 1
            numInfected -= 1
            numDeath += 1
        else:
            if rand < recoverProb:
                # patient recovers
                numInfected -= 1
                numRecovered += 1

    return numDeath, numInfected, numRecovered, numOfSusPeople


D, I, R, S = simulateDay(5, 20, 100)

print "deaths:"
print D
print "infected:"
print I
print "recovered:"
print R
print "susceptible:"
print S
print "check:"
print D + I + R + S