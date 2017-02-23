package SimModelling_Project;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.Dataset;
import org.opensourcephysics.frames.PlotFrame;
import org.opensourcephysics.numerics.ODE;
import org.opensourcephysics.numerics.ODESolver;
import org.opensourcephysics.numerics.RK4;

public class SubmarineBallast extends AbstractSimulation implements ODE {
	PlotFrame frame = new PlotFrame("x", "y", "Submarine");
	//PlotFrame ballast = new PlotFrame("t", "litres", "Water in ballas tank");
	Circle ball = new Circle(); //Ball representing the submarine
	ODESolver odeSolver = new RK4(this);
	double state[] = new double[5]; // state of simulation
	double g = 9.81;
	double pi = 3.14;
	
	//Submarine dimensions
	double subRadius;
	double subHeight;
	double subMass;
	double subVolume;
	double subDensity; // subMass / (pi*subRadius^2 * subHeight)
	
	double deltaT = 0.1; //0.1
	double ballastTank;
	double ballastTankMax; //1500 kg/l * p_obj
	double fluidDensity; //kg/m^3
	
	double buoyancy;
	double displacedFluidVolume;
	double gravForce;
	double netForce;
	
	double displacement; //displacement
	double v; //velocity
	double a; //acceleration
	
	//Buoyancy (neutral buoyancy: Density of object equals density of liquid in which it is submerged)
	//			m_b = p_obj * v_obj (v_obj = pi*r^2 * h)
	//F_net = B - p_obj * v_obj * g ;	p_obj = density of object; v_obj = volume of object;  g = gravity	B = Force of buoyancy (N)
	//B = p*V_f*g		p = fluid density	V_f = volume of displaced fluid	g = gravity
	//V_f = weight of object / density of fluid
		// Mass of Sub = Volume of Displaced Fluid = Volume Underwater?
	//F_net = p*V_f * g -  m_b * g
	//p = 1029 kg/m^3, seawater, 1000 kg/m^3, freshwater

	
	public SubmarineBallast(){
		
		frame.addDrawable(ball);
		frame.setPreferredMinMax(-10.0, 10.0, -50.0, 10.0);

		//ballast.setConnected(0, true);
		//ballast.setMarkerShape(0, Dataset.NO_MARKER);
		//ballast.setPreferredMinMax(0.0, 1000.0, 0.0, 1000.0);

	}
	
	public void initialize(){
		odeSolver.initialize(control.getDouble("deltaT"));
		subMass = control.getDouble("subMass");
		fluidDensity = control.getDouble("fluidDensity");
		subRadius = control.getDouble("subRadius");
		subHeight = control.getDouble("subHeight");
		ballastTank = control.getDouble("ballastTank");
		ballastTankMax = control.getDouble("ballastTankMax");
		displacement = control.getDouble("displacement");
		//displacement = -displacement;
		
		//subVolume = pi*(control.getDouble("subRadius") * control.getDouble("subRadius")) * control.getDouble("subHeight");
		//subDensity = (control.getDouble("subMass") / subVolume) + ballastTank;
		//subDensity = subMass / subVolume; //+ ballastTank;
		//buoyancy = control.getDouble("fluidDensity") * subDensity * g;
		subMass = control.getDouble("subMass");
		//subVolume = pi*(control.getDouble("subRadius") * control.getDouble("subRadius")) * control.getDouble("subHeight");
		//subVolume = subMass / control.getDouble("fluidDensity");
		subVolume = 3.14 * (subRadius * subRadius) * subHeight;

		subDensity = (subMass / subVolume);
		displacedFluidVolume = (g * subDensity)/(control.getDouble("fluidDensity") * g);
		
		//buoyancy = control.getDouble("fluidDensity") * displacedFluidVolume * g;
		//buoyancy = control.getDouble("fluidDensity") * displacedFluidVolume * g;
		buoyancy = control.getDouble("fluidDensity") * g;
		gravForce = (subDensity + ballastTank) * g;
		netForce = buoyancy - gravForce;
		
		state[0] = buoyancy - gravForce; //netForce
		state[1] = 0.0; //acceleration
		state[2] = 0.0; //velocity
		state[3] = -displacement; //displacement
		state[4] = 0.0; //time
		
	}
	
	@Override
	public void doStep() {
		odeSolver.step();
		subMass = control.getDouble("subMass");
		
		subVolume = 3.14 * (subRadius * subRadius) * subHeight;
		subDensity = (subMass / subVolume);
		displacedFluidVolume = (g * subDensity)/(control.getDouble("fluidDensity") * g);
		buoyancy = control.getDouble("fluidDensity") * g;
		gravForce = (subDensity + ballastTank) * g;
		netForce = buoyancy - gravForce;

		
		//To enforce the maximum ballast tank amount
		if(ballastTank > ballastTankMax){
			ballastTank = ballastTankMax;
		}
		
		/*
		if(state[4] > 40.0){
			ballastTank = 0.0;
		}*/
		
		//TODO: Check if submarine is at surface and stop it completely from moving
		if(state[3] > 0.0){
			state[3] = 0.0;
		}
		
		ball.setXY(0, state[3]);
		
	}
	
	public double[] getState() {
		return state;
	}

	public void getRate(double[] state, double[] rate) {		
		state[0] = netForce; //netForce
		//state[1] = (state[0] / subMass) * deltaT; //acceleration
		//state[2] = state[1] * deltaT; //velocity
		//state[3] = state[2] * deltaT; //displacement*/
		
		//rate[0] = state[0]*deltaT;
		state[1] = state[0]/subMass;
		rate[2] = state[1]*deltaT;
		rate[3] = state[2]*deltaT;
		rate[4] = 1.0; //time
		
		System.out.print(subVolume);
		System.out.print(" ");
		System.out.print(subDensity);
		System.out.print(" ");
		System.out.print(buoyancy);
		System.out.print(" ");
		System.out.print(gravForce);
		System.out.print(" ");
		System.out.print(state[1]); //acceleration
		System.out.print(" ");
		System.out.print(state[2]); //velocity
		System.out.print(" ");
		System.out.print(state[3]); //displacement
		System.out.print(" ");
		System.out.print(state[4]);
		System.out.print(" ");
		System.out.println(state[0]);
		
	}
	

	
	public void reset(){
		control.setValue("deltaT", 0.1);
		control.setValue("subHeight", 7.1);
		control.setValue("subRadius", 2.6);
		control.setValue("subMass", 17000);
		control.setValue("fluidDensity", 1029);
		control.setValue("ballastTank", 0.0);
		control.setValue("ballastTankMax", 10000);
		control.setValue("displacement", 0);
	}

	public static void main(String args[]){
		SimulationControl.createApp(new SubmarineBallast());
	}

}                                                                                     
