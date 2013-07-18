package com.blahsoft.designpatterns;


public interface ObservableInterface {
	void registerObserver(ObserverInterface o);
	void removeObserver(ObserverInterface o);
	void notifyObservers();
	// N.B The collection of observers should always be implemented as a collection of weak references. 
}
