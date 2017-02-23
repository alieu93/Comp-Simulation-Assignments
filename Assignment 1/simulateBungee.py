#name: Adam Lieu
#student ID: 100451790
#Description: Part 1C of Assignment 1, adding in the effects of the spring (bungee cord)

import itertools as it
import numpy as np
import scipy as sp
import pylab as pl
import matplotlib.pyplot as plt


def simulateBungee(mass, simulationTime, deltaT, surfaceArea, unstretchedBungeeLength):
    # F_weight = mass * g
    # F_friction = -0.65 * surfaceArea * v * abs(v)
    # use 0.2m^2 for surface area
    # acceleration = F_total / mass
    # F_total = F_weight + F_friction
    # F_Spring = -k * d (k = 21.7)(d = displacement)


    F_weight = mass * 9.81
    k_cons = 21.7

    deltaVel = 0  # initial velocity should be 0?
    deltaD = 0  # change in distance
    #accel = 9.81  # m/s^2, not constant anymore
    elapsedTime = []
    length = []
    velocity = []
    acceleration = []

    for t in pl.frange(0, simulationTime, deltaT):
        #print t
        # t can be considered elapsedTimet

        # Store time step values
        elapsedTime.append(t)

        # Calculate distance and update value
        deltaD += deltaVel * deltaT
        length.append(deltaD)

        # Friction and acceleration calculations
        F_friction = -0.65 * surfaceArea * deltaVel * abs(deltaVel)
        # Hooke's Law
        F_spring = -k_cons * (deltaD - unstretchedBungeeLength)
        F_total = F_weight + F_friction + F_spring
        accel = F_total / mass

        # Calculate Delta Velocity and update value
        deltaVel += accel * deltaT
        velocity.append(deltaVel)

        acceleration.append(accel)

    #print elapsedTime
    #print length
    #print velocity
    #print acceleration

    return elapsedTime, length, velocity, acceleration

elapsed, distance, vel, acc = simulateBungee(70, 60, 0.01, 0.2, 30)

plt.plot(elapsed, distance, "g^")
plt.xlabel('Elapsed Time')
plt.ylabel('Distance')
plt.title('Bungee Jump')
plt.show()