package edu.pacific.comp55.starter;

public class MainApplication extends GraphicsApplication {
	public static final int WINDOW_WIDTH = 810;
	public static final int WINDOW_HEIGHT = 640;
	public static final String MUSIC_FOLDER = "sounds";

	private Overlay overlay;
	private LeaderBoardPane leaderBoard;
	private SettingsPane settingsPane;
	private MenuPane menu;
	private BaseRoom base;
	private PauseMenu pause;
	private PauseSettingsPane pauseSetting;
	private GameInstructionPanel instruction;
	private TrafficJamPhase1 phase1;
	private TrafficJamPhase2 phase2;
	private TrafficJamPhase3 phase3;
	private TrafficJamPhase4 phase4;
	private RightRoom right;
	private MazeGraphic maze;
	private PacmanGraphic pacman;
	private BottomRoom bottom;
	private TopRoom top;
	private DeathMenu death;
	private CreditPane credit;

	private boolean playMusic = true;

	private boolean isInLeaderBoard;
	private boolean isInbaseRoom;
	private boolean isInPauseMenu;
	private boolean isInMainMenu;
	private boolean isInSettingMenu;
	private boolean isInPauseSettings;
	private boolean isInGameInstruction;
	private boolean isInTrafficJamPhase1;
	private boolean isInTrafficJamPhase2;
	private boolean isInTrafficJamPhase3;
	private boolean isInTrafficJamPhase4;
	private boolean isInRightRoom;
	private boolean isInMaze;
	private boolean isInPacman;
	private boolean isInBottomRoom;
	private boolean isInTopRoom;
	private boolean isInCredit;

	// Avoid triggering buttons in the same locations
	public boolean isInMainMenu() {
		return isInMainMenu;
	}

	public void setMazeState(boolean isInMaze) {
		this.isInMaze = isInMaze;
	}

	public boolean isInCredit() {
		return isInCredit;
	}

	public void setCreditState(boolean isInCredit) {
		this.isInCredit = isInCredit;
	}

	public boolean isInMaze() {
		return isInMaze;
	}

	public void setPacmanState(boolean isInPacman) {
		this.isInPacman = isInPacman;
	}

	public boolean isInPacman() {
		return isInPacman;
	}

	public void setMainMenuState(boolean isInMainMenu) {
		this.isInMainMenu = isInMainMenu;
	}

	public boolean isInGameInstruction() {
		return isInGameInstruction;
	}

	public void setGameInstructionState(boolean isInGameInstruction) {
		this.isInGameInstruction = isInGameInstruction;
	}

	public void setGraphicMaze(boolean isInMaze) {
		this.isInMaze = isInMaze;
	}

	public void setTrafficJamPhase1(boolean isInTrafficJamPhase1) {
		this.isInTrafficJamPhase1 = isInTrafficJamPhase1;
	}

	public boolean isInTrafficJamPhase1() {
		return isInTrafficJamPhase1;
	}

	public void setTrafficJamPhase2(boolean isInTrafficJamPhase2) {
		this.isInTrafficJamPhase2 = isInTrafficJamPhase2;
	}

	public boolean isInTrafficJamPhase2() {
		return isInTrafficJamPhase2;
	}

	public void setTrafficJamPhase3(boolean isInTrafficJamPhase3) {
		this.isInTrafficJamPhase3 = isInTrafficJamPhase3;
	}

	public boolean isInTrafficJamPhase3() {
		return isInTrafficJamPhase3;
	}

	public void setTrafficJamPhase4(boolean isInTrafficJamPhase4) {
		this.isInTrafficJamPhase4 = isInTrafficJamPhase4;
	}

	public boolean isInTrafficJamPhase4() {
		return isInTrafficJamPhase4;
	}

	public boolean isInbaseRoom() {
		return isInbaseRoom;
	}

	public void setBaseRoomState(boolean isInbaseRoom) {
		this.isInbaseRoom = isInbaseRoom;
	}

	public boolean isIntopRoom() {
		return isInTopRoom;
	}

	public void setTopRoomState(boolean isIntopRoom) {
		this.isInTopRoom = isIntopRoom;
	}

	public boolean isInTopRoom() {
		return isInTopRoom;
	}

	public boolean isInSettingMenu() {
		return isInSettingMenu;
	}

	public void setSettingMenuState(boolean isInSettingMenu) {
		this.isInSettingMenu = isInSettingMenu;
	}

	public boolean isInPauseMenu() {
		return isInPauseMenu;
	}

	public void setPauseMenuState(boolean isInPauseMenu) {
		this.isInPauseMenu = isInPauseMenu;
	}

	public boolean isInPauseSettings() {
		return isInPauseSettings;
	}

	public void setPauseSettingsState(boolean isInPauseSettings) {
		this.isInPauseSettings = isInPauseSettings;
	}

	public boolean isInLeaderBoard() {
		return isInLeaderBoard;
	}

	public void setLeaderBoardState(boolean isInLeaderBoard) {
		this.isInLeaderBoard = isInLeaderBoard;
	}

	public MainApplication() {

	}

	public void showMainMenu() {
		menu.showContents();
	}

	public void openSettings() {
		menu.hideContents();
		settingsPane.showContents();
	}

	public void closeSettings() {
		settingsPane.hideContents();
		menu.showContents();
	}

	public void openInstruction() {
		menu.showContents();
	}

	public void quitGame() {

	}

	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void run() {
		setupInteractions();
		startGame();
		switchToMenu();
		leaderBoard = new LeaderBoardPane(this);

	}

	public void startGame() {
		System.out.println("Hello, world!");

		overlay = new Overlay(this);
		base = new BaseRoom(this);
		right = new RightRoom(this);
		phase1 = new TrafficJamPhase1(this);
		phase2 = new TrafficJamPhase2(this);
		phase3 = new TrafficJamPhase3(this);
		phase4 = new TrafficJamPhase4(this);
		bottom = new BottomRoom(this);
		top = new TopRoom(this);
		pauseSetting = new PauseSettingsPane(this);

		settingsPane = new SettingsPane(this);

		menu = new MenuPane(this);

		pause = new PauseMenu(this);

		instruction = new GameInstructionPanel(this);

		maze = new MazeGraphic(this);
		pacman = new PacmanGraphic(this);
		death = new DeathMenu(this);
		credit = new CreditPane(this);

		resumeMusic();
	}

	public void deleteGame() {
		overlay = null;
		base = null;
		right = null;
		phase1 = null;
		phase2 = null;
		phase3 = null;
		phase4 = null;
		bottom = null;
		top = null;
		pauseSetting = null;
		settingsPane = null;
		menu = null;
		pause = null;
		instruction = null;
		maze = null;
		pacman = null;
		death = null;
		credit = null;

	}

	public void switchToBaseRoom() {
		setBaseRoomState(true);
		switchToScreen(base);
	}

	public void switchToRightRoom() {
		setRightRoomState(true);
		switchToScreen(right);
	}

	public void switchToBottomRoom() {
		setBottomRoomState(true);
		switchToScreen(bottom);
	}

	public void switchToTopRoom() {
		setTopRoomState(true);
		switchToScreen(top);
	}

	public void setRightRoomState(boolean isInRightRoom) {
		this.isInRightRoom = isInRightRoom;
	}

	public boolean isInRightRoom() {
		return isInRightRoom;
	}

	public void setBottomRoomState(boolean isInBottomRoom) {
		this.isInBottomRoom = isInBottomRoom;
	}

	public boolean isInBottomRoom() {
		return isInBottomRoom;
	}

	public void switchToMenu() {
		setMainMenuState(true);
		switchToScreen(menu);
	}

	public void switchToPause() {
		setPauseMenuState(true);
		switchToScreen(pause);
	}

	public void switchToDeathMenu() {
		switchToScreen(death);
	}

	public void switchToSettings() {
		setSettingMenuState(true);
		switchToScreen(settingsPane);
	}

	public void switchToPauseSettings() {
		switchToScreen(pauseSetting);
	}

	public void switchToGameInstructionPanel() {
		switchToScreen(instruction);
	}

	public void switchToLeaderBoard() {
		setLeaderBoardState(true);
		switchToScreen(leaderBoard);
	}

	public void switchToTrafficJamPhase1() {
		setTrafficJamPhase1(true);
		switchToScreen(phase1);
	}

	public void switchToTrafficJamPhase2() {
		setTrafficJamPhase2(true);
		switchToScreen(phase2);
	}

	public void switchToTrafficJamPhase3() {
		setTrafficJamPhase3(true);
		switchToScreen(phase3);
	}

	public void switchToTrafficJamPhase4() {
		setTrafficJamPhase4(true);
		switchToScreen(phase4);
	}

	public void switchToMazeGraphic() {
		setGraphicMaze(true);
		switchToScreen(maze);
	}

	public void switchToPacmanGraphic() {
		setPacmanState(true);
		switchToScreen(pacman);
	}

	public void switchToCredit() {
		setCreditState(true);
		switchToScreen(credit);
	}

	public void playClickSound() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playSound(MUSIC_FOLDER, "clicksound.mp3");
	}

	public void playAracadeSound() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playSound(MUSIC_FOLDER, "AracadeSound.mp3");
	}

	public void playShootingSound() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playSound(MUSIC_FOLDER, "shooting.mp3");
	}

	public void playLockedSound() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playSound(MUSIC_FOLDER, "LockedDoorSound.mp3");
	}

	public void playOpenSound() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playSound(MUSIC_FOLDER, "OpenDoorSound.mp3");
	}

	public void playErrorSound() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playSound(MUSIC_FOLDER, "trafficjamerror.mp3");
	}

	public void playBugDieSound() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playSound(MUSIC_FOLDER, "bugdie.mp3");
	}

	public void stopMusic() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.pauseSound(MUSIC_FOLDER, "music.mp3");
	}

	public void resumeMusic() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playSound(MUSIC_FOLDER, "music.mp3", true);
	}

	public void setMusicState() {
		if (playMusic == false) {
			playMusic = true;
		} else {
			playMusic = false;

		}
		System.out.println(playMusic);
	}

	public void turnSoundOff() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.setMute(true);
	}

	public void turnSoundOn() {
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.setMute(false);
	}

	public BaseRoom getBase() {
		return base;
	}

	public Overlay getOverlay() {
		return overlay;
	}

	public static void main(String[] args) {
		new MainApplication().start();
	}

}
