package com.augmentum.ot.service;

import com.augmentum.ot.dataObject.Page;
import com.augmentum.ot.dataObject.ToDoItem;
import com.augmentum.ot.exception.DataWarningException;
import com.augmentum.ot.exception.ServerErrorException;
import com.augmentum.ot.model.Employee;

public interface ToDoListService {
	
	/**
	 * Get to do list for master.
	 * @return
	 * @throws DataWarningException
	 * @throws ServerErrorException
	 */
	public Page<ToDoItem> findToDoItemsForMaster(Employee employee, Integer pageNo, Integer pageSize) throws DataWarningException, ServerErrorException;
	
	/***
	 * Get to do list for trainer.
	 * @return
	 * @throws DataWarningException
	 * @throws ServerErrorException
	 */
	public Page<ToDoItem> findToDoItemsForTrainer(Employee employee, Integer pageNo, Integer pageSize) throws DataWarningException, ServerErrorException;
	
	/**
	 * Get to do list for trainee.
	 * @return
	 * @throws DataWarningException
	 * @throws ServerErrorException
	 */
	public Page<ToDoItem> findToDoItemsForTrainee(Employee employee, Integer pageNo, Integer pageSize) throws DataWarningException, ServerErrorException;
}
