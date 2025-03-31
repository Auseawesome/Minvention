package minvention;

import arc.assets.Loadable;

public class MinventionVars implements Loadable {
	// Scale Factors
	public static final float waterPerSteam = 0.2f;
	public static final float waterPerIce = 30f;
	public static final float waterPerSnow = 12f;

	// Water efficiency per tier
	public static final float waterCrudeEff = 0.5f;
	public static final float waterBasicEff = 2f/3f;
	public static final float waterImprovedEff = 0.75f;
	public static final float waterAdvancedEff = 0.9f;
	public static final float waterSealedEff = 1f;

	public static final String modPrefix = "minvention-";
}
