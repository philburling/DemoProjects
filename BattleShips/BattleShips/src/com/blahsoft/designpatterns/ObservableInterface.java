package com.blahsoft.designpatterns;

/* Use this interface to implement an observable object that sends notifications to a set of observers for changes
 * in the state of that object. */
public interface ObservableInterface {
	void registerObserver(ObserverInterface o);
	void removeObserver(ObserverInterface o);
	void notifyObservers();
	// N.B The collection of observers should always be implemented as a collection of weak references. 
}
