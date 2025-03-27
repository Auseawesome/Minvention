package minvention;

import arc.assets.Loadable;

public class Vars implements Loadable {
	// Scale Factors
	public static final float waterPerSteam = 5;
	public static final float waterPerIce = 30;
	public static final float waterPerSnow = 12;

	// Water efficiency per tier
	public static final float waterCrudeEff = 0.5f;
	public static final float waterBasicEff = 0.67f;
	public static final float waterImprovedEff = 0.75f;
	public static final float waterAdvancedEff = 0.9f;
	public static final float waterSealedEff = 1f;

	public static final String modPrefix = "minvention-";
}
