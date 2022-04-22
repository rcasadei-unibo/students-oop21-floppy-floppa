package main.game_engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;

import javax.swing.JPanel;
import javax.swing.Timer;

import main.character.Character;
import main.infinite_map.Map;
import main.menu.EOGMenuGUI;
import main.menu.MainMenu;
import main.utilities.CommonMethods;
import main.utilities.Constants;
import main.utilities.Constants.PANEL;
import main.utilities.GBCSimplified;
import main.utilities.GameSettings;
import main.utilities.Position;

public class PlayPanel extends JPanel implements ActionListener, KeyListener {
	private static final long serialVersionUID = 4739973632599419506L;
	private Timer refreshRate;
	private final MainMenu mainMenu;
	private final Map map;
	private final NicknamePanel nicknamePanel;
	private final GameSettings gameSettings;
	private final Character character;
	private Timestamp gameStart;
	private Timestamp gameEnd;

	public static int reducerTimes = 0;
	public static int incrementTimes = 0;

	public PlayPanel(MainMenu mainMenu, GameSettings gameSettings) {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(Constants.SCREEN_SIZE);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();

		this.mainMenu = mainMenu;
		this.gameSettings = gameSettings;

		map = new Map(gameSettings.getScrollingBackground());

		refreshRate = new Timer(1000 / Constants.SPEED, this);

		character = new Character(new Position((int) Constants.SCREEN_SIZE.getHeight() / 2,
				CommonMethods.getPixelsFromPercentageWidth(30)), gameSettings.getSkin());

		this.nicknamePanel = new NicknamePanel(this, gameSettings);
		this.add(nicknamePanel, new GBCSimplified(GridBagConstraints.CENTER));
	}

	public void dismissNicknamePanel() {
		this.remove(nicknamePanel);
		refreshRate.start();
		map.startTimer();

		PlayPanel.reducerTimes = 0;
		PlayPanel.incrementTimes = 0;

		gameStart = new Timestamp(System.currentTimeMillis());
	}

	private int getMetersTraveled() {
		return (int) ((gameEnd.getTime() - gameStart.getTime()) / 1000);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D canvas = (Graphics2D) g;
		map.animate(canvas);
		character.animate(canvas);

		character.collideFixedObstacle(map.getPaintedFixedObstacles());
		character.collideMovingObstacle(map.getPaintedMovingObstacles());
		character.collideMalus(map.getPaintedMalus());
		character.collideBooster(map.getPaintedBoosters());
		character.collideBorders();

		if (character.isDead()) {
			refreshRate.stop();
			map.stopTimer();

			gameEnd = new Timestamp(System.currentTimeMillis());

			mainMenu.add(PANEL.EOGMENU.name(), new EOGMenuGUI(mainMenu, getMetersTraveled(), 0, 0));
			mainMenu.showCard(PANEL.EOGMENU);

			gameSettings.getPlayer().setPersonalBest(getMetersTraveled());
			mainMenu.getLeaderboard().update(gameSettings.getPlayer());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			this.character.jump();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
