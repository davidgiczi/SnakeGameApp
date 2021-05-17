package com.david.giczi.snakegame.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.david.giczi.snakegame.domain.Component;
import com.david.giczi.snakegame.utils.Direction;
import com.david.giczi.snakegame.utils.ResponseType;

public interface SnakeGameService {

	void setGameBoardParam();
	List<Component> createSnakeComponentStore(int length);
	List<Component> createBoardComponentStore();
	List<Component> createEdibleComponentStore(List<Component> snake, List<Component> barrierStore);
	List<Component> createBarrierComponentStore(List<Component> snake);
	List<Component> addComponentStoreToBoardComponentStore(List<Component> board, List<Component> componentStore);
	List<Component> goDirect(List<Component> snake);
	List<Component> goNorth(List<Component> snake);
	List<Component> goEast(List<Component> snake);
	List<Component> goSouth(List<Component> snake);
	List<Component> goWest(List<Component> snake);
	List<Component> eating(List<Component> snake, List<Component> edibleStore, Direction direction);
	boolean canGoDirect(List<Component> snake);
	boolean canGoNorth(List<Component> snake);
	boolean canGoEast(List<Component> snake);
	boolean canGoSouth(List<Component> snake);
	boolean canGoWest(List<Component> snake);
	boolean isSnakeBittenByItself(List<Component> snake);
	boolean isComponentMeeting(List<Component> snake, List<Component> componentStore, Direction direction);
	int calcScore(List<Component> snake);
	String calcLevel(List<Component> snake);
	int getTempo(List<Component> snake);
	void createAndSendResponseString(HttpServletRequest request, HttpServletResponse response, ResponseType type);
	
}