package org.wolf.other;

import java.io.Serializable;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;
	/*
	 * Типы сообщений
	 * от всех:
	 * exit - означает, что клиент/сервер вышел.
	 * register - регистрация клиента
	 * programm - текст алгоритма
	 * 
	 * только от сервера:
	 * map - пересылает карту 
	 * step - текущее положение корабликов 
	 * message - обычное оповещение 
	 * addPlayer - добавление нового игрока 
	 * deletePlayer - игрок вышел
	 * error - произошла ошибка 
	 */
	public String type = "";
	public SPlayer player = null;
	public String name = "";
	public String text = "";
	public byte[][] algorithm;
	public SPlayer[] players = new SPlayer[10];
	public int size = 0;
	public byte[][] map;
	
	public Message(String name, String type, String text)
	{
		this.name = name;
		this.type = type;
		this.text = text;
		player = null;
	}
	
	public Message(String type)
	{
		this.type = type;
	}
	
	public Message()
	{
		name = "";
		type = "";
		text = "";
		player = null;
	}
	
	public void addPlayer(SPlayer p)
	{
		if (size<5)
			players[size++] = p;
	}
}
