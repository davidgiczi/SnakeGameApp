package com.david.giczi.snakegame.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "param")
public class GameParam {

	private int board_rows;
	private int board_cols;
	private int snake_length;
	private int edible_number;
	private int barrier_number;
	
	
	public int getBoard_rows() {
		return board_rows;
	}
	
	public void setBoard_rows(int board_rows) {
		this.board_rows = board_rows;
	}

	public int getBoard_cols() {
		return board_cols;
	}
	public void setBoard_cols(int board_cols) {
		this.board_cols = board_cols;
	}
	public int getSnake_length() {
		return snake_length;
	}
	public void setSnake_length(int snake_length) {
		this.snake_length = snake_length;
	}
	public int getBarrier_number() {
		return barrier_number;
	}
	public void setBarrier_number(int barrier_number) {
		this.barrier_number = barrier_number;
	}

	public int getEdible_number() {
		return edible_number;
	}

	public void setEdible_number(int edible_number) {
		this.edible_number = edible_number;
	}
	
		
	
}
