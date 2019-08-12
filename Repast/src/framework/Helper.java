package framework;

import com.google.common.primitives.Doubles;

import repast.simphony.random.RandomHelper;

public final class Helper {
	public static double normalRanged(double mean, double sd) {
		return Doubles.constrainToRange(RandomHelper.createNormal(mean, sd).nextDouble(), 0, 1);
	}
	
	public static double normalRanged(double mean, double sd, double min, double max) {
		return Doubles.constrainToRange(RandomHelper.createNormal(mean, sd).nextDouble(), min, max);
	}

	public static StrengthValues normalRangedStrengthValues(double d, double e) {
		return new StrengthValues(normalRanged(d,e),normalRanged(d,e),normalRanged(d,e));
	}

	public static StrengthValues normalRangedStrengthValues(double i, double d, double j, double k) {
		return new StrengthValues(normalRanged(i,d,j,k),
				normalRanged(i,d,j,k),
				normalRanged(i,d,j,k));
	}
}
