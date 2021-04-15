package com.sobey.module.evaluate.model.request;

import java.util.List;

import com.sobey.module.evaluate.model.Evaluate;
import com.sobey.module.workOrder.model.WorkOrder;

/**
 */
public class EvaluateRequest {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;


	private WorkOrder workOrder;
	private List<Evaluate> list;
	/**
	 * @return the workOrder
	 */
	public WorkOrder getWorkOrder() {
		return workOrder;
	}
	/**
	 * @param workOrder the workOrder to set
	 */
	public void setWorkOrder(WorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	/**
	 * @return the list
	 */
	public List<Evaluate> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<Evaluate> list) {
		this.list = list;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
	

}
