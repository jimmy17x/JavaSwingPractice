package controller;

//sort of a simulated message server


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.Message;

public class MessageServer implements Iterable <Message>{
	private Map<Integer, List<Message>> messages;
	private List<Message> selected;
	

	public MessageServer() {
		messages = new TreeMap<>();
		selected = new ArrayList<>();
		

		List<Message> list = new ArrayList<>();
		list.add(new Message("The cat is missing ", "Have u seen it ?"));
		list.add(new Message("C ya ", "r we still meeting at pub ?"));
		messages.put(0, list);

		list = new ArrayList<>();
		list.add(new Message("people fall in love in mysterious ways",
				"may b its all part of a plan"));
		list.add(new Message("i just keep on mkn same mistakes",
				"hoping tht u will understand"));
		messages.put(1, list);

	}
	
	public void setSelectedServers(Set<Integer> servers){
		selected.clear();
		for(Integer id: servers){
			if(messages.containsKey(id)){
				selected.addAll(messages.get(id));
			}
		}
	}
	
	public int getMessageCount(){
		return selected.size();
	}

	@Override
	public Iterator<Message> iterator() {
		return new MessageIterator(selected);
	}
}

class MessageIterator implements Iterator {

	private Iterator <Message> iterator;
	
	public MessageIterator(List<Message> messages){
		iterator = messages.iterator();
		
	}
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Object next() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//i dont care on interruption , thts a fuckin cancel button operation
		}
		return iterator.next();
	}

	@Override
	public void remove() {
	iterator.remove();
		
	}
	
}
