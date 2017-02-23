package Assign2_Pt1;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Dataset;
import org.opensourcephysics.frames.PlotFrame;
import org.opensourcephysics.numerics.ODE;
import org.opensourcephysics.numerics.ODESolver;
import org.opensourcephysics.numerics.RK4;

public class CarSim extends AbstractSimulation implements ODE {
	PlotFrame frame = new PlotFrame("time", "distance", "distance between vehicles");
	//ODESolver odeSolver = new RK4(this);
	ODESolver odeSolver = new RK4(this);
	double state[] = new double[7];  // state of the simulation
	
	double initialSeparation; //25.0
	double initialVelocity; //0.0
	double initialAcceleration; //4.0
	double sensitivityConstant; //30.0
	double reactionTime; //1.0
	double accelerationChangeTimeStep; //100
	double updatedAcceleration; //-2.0
	double deltaT = 0.1; //0.1
	
	double tempState[] = new double[4];

	
	public CarSim(){
		frame.setConnected(0, true);
		frame.setMarkerShape(0, Dataset.CIRCLE);
		frame.setPreferredMinMax(0.0, 150.0, 0.0, 150.0);
	}
	
	/**
	 * Initialize the state of the simulation
	 */
	public void initialize(){
		//Maybe change these to state[] = to this, initial values
		odeSolver.initialize(control.getDouble("deltaT"));
		
		initialSeparation = control.getDouble("initialSeparation");
		initialVelocity = control.getDouble("initialVelocity");
		initialAcceleration = control.getDouble("initialAcceleration");
		
		sensitivityConstant = control.getDouble("sensitivityConstant");
		reactionTime = control.getDouble("reactionTime");
		accelerationChangeTimeStep = control.getDouble("accelerationChangeTimeStep");
		updatedAcceleration = control.getDouble("updatedAcceleration");
		// initialize and set the step size for the ODE solver
		
		// set the initial state of the simulation
		//Assuming all initial values are for the lead car
		state[0] = control.getDouble("initialSeparation"); //position of lead car
		state[1] = 0.0; //Position of following car
		state[2] = 0.0;
		state[3] = control.getDouble("initialVelocity"); // Velocity of following car
		state[3] = 0.0;
		state[4] = control.getDouble("initialAcceleration"); // Accel of lead car
		state[5] = 0.0;
		state[6] = 0.0; //t
		
		
	}

	/**
	 * Return the state of the simulation
	 */
	public double[] getState() {
		return state;
	}
	
	/**
	 * sets the simulation parameters to their initial values
	 */
	public void reset() {
		control.setValue("initialSeparation", 25.0);
		control.setValue("initialVelocity", 0.0);
		control.setValue("initialAcceleration", 4.0);
		control.setValue("sensitivityConstant", 30.0);
		control.setValue("reactionTime", 1.0);
		control.setValue("accelerationChangeTimeStep", 100);
		control.setValue("updatedAcceleration", -2.0);
		control.setValue("deltaT", 0.1);
		initialize();
	}

	public void getRate(double[] state, double[] rate) {	
		//rate[5] = (sensitivityConstant*(state[2]-state[3])/(state[0]-state[1]));
		rate[5] = state[5];
		
		// Distance of leading car
		rate[0] = state[2]*deltaT;
		// Distance of following car, should always be 0
		rate[1] = state[3]*deltaT;
		// Velocity of leading car
		rate[2] = state[4]*deltaT;
		// Velocity of following car
		rate[3] = state[5]*deltaT;
		// Acceleration of leading car
		rate[4] = 0.0;
		// Acceleration of following car
		rate[5] = 0.0;
		// Time
		rate[6] = 1.0;
		
		
		System.out.print(sensitivityConstant);
		System.out.print("  ");
		System.out.print(state[0]);
		System.out.print("  ");
		System.out.print(state[1]);
		System.out.print("  ");
		System.out.print(state[2]);
		System.out.print("  ");
		System.out.print(state[3]);
		System.out.print("  ");
		System.out.println(state[6]);
	}

	@Override
	protected void doStep() {
		// TODO Auto-generated method stub
		odeSolver.step();
		tempState[0] = state[0];
		tempState[1] = state[1];
		tempState[2] = state[2];
		tempState[3] = state[3];
		
		state[5] = (sensitivityConstant*(tempState[2]-tempState[3])/(tempState[0]-tempState[1]));
		
		if(state[6] >= control.getDouble("accelerationChangeTimeStep")){
			state[4] = control.getDouble("updatedAcceleration");
		}
		frame.append(0, state[6], state[0]-state[1]);
		frame.append(1, state[6], state[1]-state[1]);
	}
	
	/**
	 * start the simulation application.
	 * @param args command line parameters (not used)
	 */
	public static void main(String args[]) {
		SimulationControl.createApp(new CarSim());
	}

}
