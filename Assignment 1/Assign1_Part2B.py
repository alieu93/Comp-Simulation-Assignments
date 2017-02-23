#Name: Adam Lieu
#Student ID: 100451790
#Description:
#Part 2B of Assignment 1, simulate the spread of an infectious disease

# Constant Spread Rate: 0.15
# Fatality rate: 0.025
# Recovery rate: 0.15

import itertools as it
import numpy as np
import scipy as sp
import pylab as pl
import matplotlib.pyplot as plt
import random

def simulateNDays(days, numContacts, numInfected, numPeople):
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

    # Set up lists
    listDeaths = []
    listInfected= []
    listRecovered = []
    listSusceptible = []

    spreadProb = 0.15
    deathProb = 0.025
    recoverProb = 0.15

    numRecovered = 0
    numDeath = 0

    #Simulate if the susceptible person becomes infected
    infectionRate = (spreadProb * numContacts * numInfected) / numPeople
    #print infectionRate

    #Number of susceptible people
    numOfSusPeople = numPeople - numInfected

    # initial values for lists
    listDeaths.append(0)
    listInfected.append(numInfected)
    listRecovered.append(0)
    listSusceptible.append(numOfSusPeople)
    #Simulate if suspectible person gets infected or not

    # loop for however many days
    for i in range(days):
        for j in range(numOfSusPeople):
            rand = random.random()
            if rand < infectionRate:
                numInfected += 1
                numOfSusPeople -= 1



        #Simulate if infected person recovers or dies
        for j in range(numInfected):
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

        # store results for each day
        listDeaths.append(numDeath)
        listInfected.append(numInfected)
        listRecovered.append(numRecovered)
        listSusceptible.append(numOfSusPeople)

    return listDeaths, listInfected, listRecovered, listSusceptible


day = range(0, 51)

# initial number of infected people = 100
# With initial total number of people = 100
# simulate for 50 days
D, I, R, S = simulateNDays(50, 5, 100, 100)


plt.plot(day, D, "rs")
plt.plot(day, I, "y^")
plt.plot(day, R, "go")
plt.plot(day, S, "+")
plt.xlabel('Day')
plt.ylabel('Count')
plt.title('Spread of Infection')
plt.legend(['Fatalities', 'Infected', 'Recovered', 'Susceptible'], loc='upper right')
plt.show()

