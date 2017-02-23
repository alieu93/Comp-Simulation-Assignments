#name: Adam Lieu
#student ID: 100451790
#description: Part 1A of Assignment 1, just simulating free fall without
# friction or the effect of the bungee

import itertools as it
import numpy as np
import scipy as sp
import pylab as pl
import matplotlib.pyplot as plt
from numpy import arange


def simulateFreeFall(mass, simulationTime, deltaT):
    deltaVel = 0  # initial velocity should be 0?
    deltaD = 0 # change in distance
    accel = 9.81 #m/s^2, constant
    elapsedTime = []
    length = []
    velocity = []
    acceleration = []

    for t in pl.frange(0, simulationTime, deltaT):
        #print t
        # t can be considered elapsedTime
        elapsedTime.append(t)

        deltaD += deltaVel * deltaT
        length.append(deltaD)

        deltaVel += accel * deltaT
        velocity.append(deltaVel)

        acceleration.append(accel)

    #print elapsedTime
    #print length
    #print velocity

    return elapsedTime, length, velocity, acceleration

elasped, distance, vel, acc = simulateFreeFall(70, 60, 0.01)

plt.plot(elasped, distance, "rs")
plt.xlabel('Elapsed Time')
plt.ylabel('Distance')
plt.title('Free Fall - No Friction')
plt.show()

