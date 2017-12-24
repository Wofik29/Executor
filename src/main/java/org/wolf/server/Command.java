package org.wolf.server;

interface Command 
{
	abstract public boolean execute(Player obj) throws Exception;
	abstract public String toString();
}
