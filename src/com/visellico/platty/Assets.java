package com.visellico.platty;

public class Assets {
	
	//These are for when I loaded using the class resource loader. Not the biggest fan! I can see how it could be useful if you wanted to keep your crap inside the jar file
//	public static String dirDefaultLevels = "/Levels/Default";
//	public static String dirCustomLevels = "/Levels/Custom";
//	public static String dirDefaultLevelTypes = "/Level Types/Default";
//		public static String defaultLevelType = "/Level Types/Default/Debug";
//	public static String dirCustomLevelTypes = "/Level Types/Custom";
	
	public static String dirDefaultLevels = "/res/Levels/Default";
	public static String dirCustomLevels = "/res/Levels/Custom";
	public static String dirDefaultLevelTypes = "/res/Level Types/Default";
		public static String defaultLevelType = "/res/Level Types/Default/Debug";
	public static String dirCustomLevelTypes = "/res/Level Types/Custom";
	
	public static String prgmDir = System.getProperty("user.dir");
	

	private Assets() {
	}
	
}
