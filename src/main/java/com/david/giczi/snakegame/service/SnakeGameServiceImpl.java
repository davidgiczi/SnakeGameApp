package com.david.giczi.snakegame.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.david.giczi.snakegame.config.Config;
import com.david.giczi.snakegame.config.GameParam;
import com.david.giczi.snakegame.domain.Component;
import com.david.giczi.snakegame.utils.Direction;
import com.david.giczi.snakegame.utils.ResponseType;
import com.david.giczi.snakegame.utils.ComponentColor;

@Service
public class SnakeGameServiceImpl implements SnakeGameService, ComponentColor {

	private GameParam param;

	@Autowired
	public void setParam(GameParam param) {
		this.param = param;
	}

	@Override
	public void setGameBoardParam() {

		Config.BOARD_ROWS = param.getBoard_rows();
		Config.BOARD_COLS = param.getBoard_cols();
		Config.SNAKE_LENGTH = param.getSnake_length();
		Config.EDIBLE_NUMBER = param.getEdible_number();
		Config.BARRIER_NUMBER = param.getBarrier_number();

	}

	@Override
	public List<Component> createBoardComponentStore() {

		List<Component> board = new ArrayList<>();

		for (int i = 0; i < Config.BOARD_ROWS * Config.BOARD_COLS; i++) {

			board.add(new Component(i));
		}

		return board;
	}

	@Override
	public List<Component> createSnakeComponentStore(int length) {

		List<Component> snake = new ArrayList<>();

		if (0 > length || length > Config.BOARD_ROWS * Config.BOARD_COLS) {
			return snake;
		}

		for (int i = 0; i < length - 1; i++) {
			Component snakeBody = new Component(i);
			snakeBody.setColor(ComponentColor.YELLOW);
			snake.add(snakeBody);
		}

		Component snakeHead = new Component(length - 1);
		snakeHead.setActualDirection(Direction.EAST);
		snakeHead.setColor(ComponentColor.RED);
		snake.add(snakeHead);

		return snake;
	}

	@Override
	public List<Component> addComponentStoreToBoardComponentStore(List<Component> board,
			List<Component> componentStore) {

		for (Component boardComponent : board) {
			for (Component storeComponent : componentStore) {

				if (boardComponent.equals(storeComponent)) {

					boardComponent.setColor(storeComponent.getColor());
				}

			}
		}

		return board;
	}

	@Override
	public boolean canGoDirect(List<Component> snake) {

		Component snakeHead = stepDirect(new Component(snake.get(snake.size() - 1).getLogicBoardIndex()));
		snakeHead.setActualDirection(snake.get(snake.size() - 1).getActualDirection());
	
		if (snakeHead.getActualDirection() == Direction.NORTH && snakeHead.getViewBoard_x() < 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.EAST
				&& snakeHead.getViewBoard_y() >= Config.BOARD_COLS - 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.SOUTH
				&& snakeHead.getViewBoard_x() >= Config.BOARD_ROWS - 1) {
			return false;

		} else if (snakeHead.getActualDirection() == Direction.WEST 
				&& snakeHead.getViewBoard_y() < 1) {
			return false;
		}

		return true;
	}

	@Override
	public List<Component> goDirect(List<Component> snake) {

		List<Component> steppedSnake = new ArrayList<>();
		snake.stream().filter(c -> snake.indexOf(c) > 0)
				.forEach(c -> steppedSnake.add(new Component(c.getLogicBoardIndex())));
		steppedSnake.forEach(c -> c.setColor(ComponentColor.YELLOW));
		Component snakeHead = snake.get(snake.size() - 1);
		steppedSnake.add(stepDirect(snakeHead));
		Component deletedSnakeComponent = snake.get(0);
		steppedSnake.add(deletedSnakeComponent);

		return steppedSnake;
	}

	private Component stepDirect(Component snakeComponent) {

		Direction actual = snakeComponent.getActualDirection();

		if (actual == Direction.NORTH) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() - 1, snakeComponent.getViewBoard_y());

		} else if (actual == Direction.EAST) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() + 1);

		} else if (actual == Direction.SOUTH) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() + 1, snakeComponent.getViewBoard_y());

		} else if (actual == Direction.WEST) {

			snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() - 1);
		}

		return snakeComponent;
	}

	@Override
	public boolean canGoNorth(List<Component> snake) {

		Component snakeHead = snake.get(snake.size() - 1);

		if (snakeHead.getViewBoard_x() < 1) {
			return false;
		}

		return true;
	}

	@Override
	public List<Component> goNorth(List<Component> snake) {

		Direction actual = snake.get(snake.size() - 1).getActualDirection();

		if (actual == Direction.EAST || actual == Direction.WEST) {

			List<Component> steppedSnake = new ArrayList<>();
			snake.stream().filter(c -> snake.indexOf(c) > 0)
					.forEach(c -> steppedSnake.add(new Component(c.getLogicBoardIndex())));
			steppedSnake.forEach(c -> c.setColor(ComponentColor.YELLOW));
			Component snakeHead = snake.get(snake.size() - 1);
			steppedSnake.add(stepNorth(snakeHead));
			Component deletedSnakeComponent = snake.get(0);
			steppedSnake.add(deletedSnakeComponent);
			return steppedSnake;
		}
		snake.add(new Component(-1));
		return snake;
	}

	private Component stepNorth(Component snakeComponent) {

		snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() - 1, snakeComponent.getViewBoard_y());
		snakeComponent.setActualDirection(Direction.NORTH);

		return snakeComponent;
	}

	@Override
	public boolean canGoEast(List<Component> snake) {

		Component snakeHead = snake.get(snake.size() - 1);

		if (snakeHead.getViewBoard_y() >= Config.BOARD_COLS - 1) {
			return false;
		}

		return true;
	}

	@Override
	public List<Component> goEast(List<Component> snake) {

		Direction actual = snake.get(snake.size() - 1).getActualDirection();

		if (actual == Direction.SOUTH || actual == Direction.NORTH) {

			List<Component> steppedSnake = new ArrayList<>();
			snake.stream().filter(c -> snake.indexOf(c) > 0)
					.forEach(c -> steppedSnake.add(new Component(c.getLogicBoardIndex())));
			steppedSnake.forEach(c -> c.setColor(ComponentColor.YELLOW));
			Component snakeHead = snake.get(snake.size() - 1);
			steppedSnake.add(stepEast(snakeHead));
			Component deletedSnakeComponent = snake.get(0);
			steppedSnake.add(deletedSnakeComponent);
			return steppedSnake;
		}
		snake.add(new Component(-1));
		return snake;
	}

	private Component stepEast(Component snakeComponent) {

		snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() + 1);
		snakeComponent.setActualDirection(Direction.EAST);

		return snakeComponent;
	}

	@Override
	public boolean canGoSouth(List<Component> snake) {

		Component snakeHead = snake.get(snake.size() - 1);

		if (snakeHead.getViewBoard_x() >= Config.BOARD_ROWS - 1) {
			return false;
		}

		return true;
	}

	@Override
	public List<Component> goSouth(List<Component> snake) {

		Direction actual = snake.get(snake.size() - 1).getActualDirection();

		if (actual == Direction.EAST || actual == Direction.WEST) {

			List<Component> steppedSnake = new ArrayList<>();
			snake.stream().filter(c -> snake.indexOf(c) > 0)
					.forEach(c -> steppedSnake.add(new Component(c.getLogicBoardIndex())));
			steppedSnake.forEach(c -> c.setColor(ComponentColor.YELLOW));
			Component snakeHead = snake.get(snake.size() - 1);
			steppedSnake.add(stepSouth(snakeHead));
			Component deletedSnakeComponent = snake.get(0);
			steppedSnake.add(deletedSnakeComponent);
			return steppedSnake;
		}
		snake.add(new Component(-1));
		return snake;
	}

	private Component stepSouth(Component snakeComponent) {

		snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x() + 1, snakeComponent.getViewBoard_y());
		snakeComponent.setActualDirection(Direction.SOUTH);

		return snakeComponent;
	}

	@Override
	public boolean canGoWest(List<Component> snake) {

		Component snakeHead = snake.get(snake.size() - 1);

		if (snakeHead.getViewBoard_y() < 1) {
			return false;
		}

		return true;
	}

	@Override
	public List<Component> goWest(List<Component> snake) {

		Direction actual = snake.get(snake.size() - 1).getActualDirection();

		if (actual == Direction.SOUTH || actual == Direction.NORTH) {

			List<Component> steppedSnake = new ArrayList<>();
			snake.stream().filter(c -> snake.indexOf(c) > 0)
					.forEach(c -> steppedSnake.add(new Component(c.getLogicBoardIndex())));
			steppedSnake.forEach(c -> c.setColor(ComponentColor.YELLOW));
			Component snakeHead = snake.get(snake.size() - 1);
			steppedSnake.add(stepWest(snakeHead));
			Component deletedSnakeComponent = snake.get(0);
			steppedSnake.add(deletedSnakeComponent);
			return steppedSnake;

		}
		snake.add(new Component(-1));
		return snake;
	}

	private Component stepWest(Component snakeComponent) {

		snakeComponent.setViewBoardCoords(snakeComponent.getViewBoard_x(), snakeComponent.getViewBoard_y() - 1);
		snakeComponent.setActualDirection(Direction.WEST);

		return snakeComponent;
	}

	@Override
	public boolean isSnakeBittenByItself(List<Component> snake) {

		List<Component> alterSnake = new ArrayList<>(snake);

		Component snakeHead = alterSnake.get(snake.size() - 1);
		alterSnake.remove(snake.size() - 1);
		if (alterSnake.contains(snakeHead)) {
			return true;
		}

		return false;
	}

	@Override
	public List<Component> createEdibleComponentStore(List<Component> snake, List<Component> barrierStore) {

		Component snakeHead = snake.get(snake.size() - 1);
		List<Component> store = new ArrayList<>();

		while (store.size() < Config.EDIBLE_NUMBER) {

			int x = (int) (Math.random() * Config.BOARD_ROWS);
			int y = (int) (Math.random() * Config.BOARD_COLS);

			Component component = new Component(x, y);

			if (x != snakeHead.getViewBoard_x() && y != snakeHead.getViewBoard_y() && !snake.contains(component)
					&& !store.contains(component) && !barrierStore.contains(component)
					&& !isEdibleComponentSurroundedByMoreThanOneBarriers(barrierStore, component)) {

				component.setColor(ComponentColor.GREEN);

				store.add(component);
			}

		}

		return store;
	}

	private boolean isEdibleComponentSurroundedByMoreThanOneBarriers(List<Component> barrierStore,
			Component component) {

		int numberOfBarriersNextToEdible = 0;

		if (barrierStore.contains(new Component(component.getViewBoard_x() + 1, component.getViewBoard_y()))) {
			numberOfBarriersNextToEdible++;
		}
		if (barrierStore.contains(new Component(component.getViewBoard_x() - 1, component.getViewBoard_y()))) {
			numberOfBarriersNextToEdible++;
		}
		if (barrierStore.contains(new Component(component.getViewBoard_x(), component.getViewBoard_y() + 1))) {
			numberOfBarriersNextToEdible++;
		}
		if (barrierStore.contains(new Component(component.getViewBoard_x(), component.getViewBoard_y() - 1))) {
			numberOfBarriersNextToEdible++;
		}

		if (numberOfBarriersNextToEdible > 1) {
			return true;
		}

		return false;
	}

	@Override
	public List<Component> createBarrierComponentStore(List<Component> snake) {

		Component snakeHead = snake.get(snake.size() - 1);
		List<Component> store = new ArrayList<>();

		while (store.size() < Config.BARRIER_NUMBER) {

			int x = (int) (Math.random() * Config.BOARD_ROWS);
			int y = (int) (Math.random() * Config.BOARD_COLS);

			Component component = new Component(x, y);

			if (x != snakeHead.getViewBoard_x() && y != snakeHead.getViewBoard_y() && !snake.contains(component)
					&& !store.contains(component)) {

				component.setColor(ComponentColor.BROWN);

				store.add(component);
			}

		}

		return store;
	}

	@Override
	public int calcScore(List<Component> snake) {

		int score = 0;

		for (int i = 0; i < snake.size() - Config.SNAKE_LENGTH; i++) {

			if (i < Config.EDIBLE_NUMBER) {
				score += 10;
			} else if (Config.EDIBLE_NUMBER <= i && i < 2 * Config.EDIBLE_NUMBER) {
				score += 20;
			} else if (2 * Config.EDIBLE_NUMBER <= i && i < 3 * Config.EDIBLE_NUMBER) {
				score += 30;
			} else if (3 * Config.EDIBLE_NUMBER <= i && i < 4 * Config.EDIBLE_NUMBER) {
				score += 40;
			} else if (4 * Config.EDIBLE_NUMBER <= i && i < 5 * Config.EDIBLE_NUMBER) {
				score += 50;
			} else if (5 * Config.EDIBLE_NUMBER <= i && i < 6 * Config.EDIBLE_NUMBER) {
				score += 60;
			} else if (6 * Config.EDIBLE_NUMBER <= i && i < 7 * Config.EDIBLE_NUMBER) {
				score += 70;
			} else if (7 * Config.EDIBLE_NUMBER <= i && i < 8 * Config.EDIBLE_NUMBER) {
				score += 80;
			} else if (8 * Config.EDIBLE_NUMBER <= i && i < 9 * Config.EDIBLE_NUMBER) {
				score += 90;
			} else {
				score += 100;
			}
		}

		return score;
	}

	@Override
	public String calcLevel(List<Component> snake) {

		int netSnakeLength = snake.size() - Config.SNAKE_LENGTH;

		if (0 <= netSnakeLength && netSnakeLength < Config.EDIBLE_NUMBER) {
			return "I.";
		} else if (Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 2 * Config.EDIBLE_NUMBER) {
			return "II.";
		} else if (2 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 3 * Config.EDIBLE_NUMBER) {
			return "III.";
		} else if (3 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 4 * Config.EDIBLE_NUMBER) {
			return "IV.";
		} else if (4 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 5 * Config.EDIBLE_NUMBER) {
			return "V.";
		} else if (5 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 6 * Config.EDIBLE_NUMBER) {
			return "VI.";
		} else if (6 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 7 * Config.EDIBLE_NUMBER) {
			return "VII.";
		} else if (7 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 8 * Config.EDIBLE_NUMBER) {
			return "VIII.";
		} else if (8 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 9 * Config.EDIBLE_NUMBER) {
			return "IX.";
		} else

			return "X.";
	}

	@Override
	public int getTempo(List<Component> snake) {

		int netSnakeLength = snake.size() - Config.SNAKE_LENGTH;

		if (0 <= netSnakeLength && netSnakeLength < Config.EDIBLE_NUMBER) {
			return 1000;
		} else if (Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 2 * Config.EDIBLE_NUMBER) {
			return 900;
		} else if (2 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 3 * Config.EDIBLE_NUMBER) {
			return 800;
		} else if (3 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 4 * Config.EDIBLE_NUMBER) {
			return 700;
		} else if (4 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 5 * Config.EDIBLE_NUMBER) {
			return 600;
		} else if (5 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 6 * Config.EDIBLE_NUMBER) {
			return 500;
		} else if (6 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 7 * Config.EDIBLE_NUMBER) {
			return 400;
		} else if (7 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 8 * Config.EDIBLE_NUMBER) {
			return 300;
		} else if (8 * Config.EDIBLE_NUMBER <= netSnakeLength && netSnakeLength < 9 * Config.EDIBLE_NUMBER) {
			return 200;
		} else

			return 100;
	}

	@Override
	public boolean isComponentMeeting(List<Component> snake, List<Component> componentStore, Direction direction) {
		
		Component snakeHead = new Component(snake.get(snake.size() - 1).getLogicBoardIndex());
		snakeHead.setActualDirection(snake.get(snake.size() - 1).getActualDirection());
		
		if (direction == Direction.DIRECT && componentStore.contains(stepDirect(snakeHead))) {
			return true;
		}
		else if (direction == Direction.NORTH && componentStore.contains(stepNorth(snakeHead))) {
			return true;
		}
		else if (direction == Direction.EAST && componentStore.contains(stepEast(snakeHead))) {
			return true;
		}
		else if (direction == Direction.SOUTH && componentStore.contains(stepSouth(snakeHead))) {
			return true;
		}
		else if (direction == Direction.WEST && componentStore.contains(stepWest(snakeHead))) {
			return true;
		}
		

		return false;
	}
	

	@Override
	public List<Component> eating(List<Component> snake, List<Component> edibleStore, Direction direction) {

		List<Component> eatenSnake = new ArrayList<>();
		Component commonComponent = null;
		
		switch (direction) {
		case DIRECT:
			commonComponent = new Component(snake.get(snake.size() -1).getLogicBoardIndex());
			commonComponent.setActualDirection(snake.get(snake.size() - 1).getActualDirection());
			commonComponent = stepDirect(commonComponent);
			break;
		case NORTH:
			commonComponent = stepNorth(new Component(snake.get(snake.size() -1).getLogicBoardIndex()));
			break;
		case EAST:
			commonComponent = stepEast(new Component(snake.get(snake.size() -1).getLogicBoardIndex()));
			break;
		case SOUTH:
			commonComponent = stepSouth(new Component(snake.get(snake.size() -1).getLogicBoardIndex()));
			break;
		case WEST:
			commonComponent = stepWest(new Component(snake.get(snake.size() -1).getLogicBoardIndex()));
			break;
		}

		edibleStore.remove(commonComponent);
		eatenSnake.add(new Component(-1));
		eatenSnake.addAll(snake);
		
		return eatenSnake;
	}

	@Override
	public void createAndSendResponseString(HttpServletRequest request, HttpServletResponse response,
			ResponseType type) {

		switch (type) {

		case FOR_STEPPING:
			createAndSendResponseStringForStepping(request, response);
			break;
		case FOR_NEW_TABLE:
			createAndSendResponseStringForNewTable(request, response);
			break;
		case FOR_THE_END_OF_THE_GAME:
			createAndSendResponseStringForTheEndOfTheGame(request, response);
			break;

		default:

		}

	}

	private void createAndSendResponseStringForStepping(HttpServletRequest request, HttpServletResponse response) {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		StringBuilder resp = new StringBuilder();
		snake.stream().filter(c -> snake.indexOf(c) < snake.size() - 1)
				.forEach(c -> resp.append(c.getLogicBoardIndex()).append("_"));
		resp.deleteCharAt(resp.toString().lastIndexOf("_"));
		Component deletedSnakeComponent = snake.get(snake.size() - 1);
		resp.append(";").append(deletedSnakeComponent.getLogicBoardIndex()).append(";").append(";").append(";");
		snake.remove(deletedSnakeComponent);
		resp.append(calcScore(snake)).append(";").append(calcLevel(snake)).append(";").append(getTempo(snake));
		request.getSession().setAttribute("snake", snake);
		try {
			response.getWriter().append(resp.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void createAndSendResponseStringForNewTable(HttpServletRequest request, HttpServletResponse response) {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");
		@SuppressWarnings("unchecked")
		List<Component> edibleStore = (List<Component>) request.getSession().getAttribute("edible");
		@SuppressWarnings("unchecked")
		List<Component> barrierStore = (List<Component>) request.getSession().getAttribute("barrier");

		StringBuilder resp = new StringBuilder();
		snake.forEach(c -> resp.append(c.getLogicBoardIndex()).append("_"));
		resp.deleteCharAt(resp.toString().lastIndexOf("_"));
		resp.append(";").append("-1").append(";");
		edibleStore.forEach(c -> resp.append(c.getLogicBoardIndex()).append("_"));
		resp.deleteCharAt(resp.toString().lastIndexOf("_"));
		resp.append(";");
		barrierStore.forEach(c -> resp.append(c.getLogicBoardIndex()).append("_"));
		resp.deleteCharAt(resp.toString().lastIndexOf("_"));
		resp.append(";").append(calcScore(snake)).append(";").append(calcLevel(snake)).append(";")
				.append(getTempo(snake));

		try {
			response.getWriter().append(resp.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void createAndSendResponseStringForTheEndOfTheGame(HttpServletRequest request,
			HttpServletResponse response) {

		@SuppressWarnings("unchecked")
		List<Component> snake = (List<Component>) request.getSession().getAttribute("snake");

		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Vége a játéknak!\nPontszám: " + calcScore(snake) + "\nSzint: "
					+ calcLevel(snake) + "\nSzeretnél új játékot játszni?");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
