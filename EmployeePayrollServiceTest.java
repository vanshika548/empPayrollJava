package main;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;



import junit.framework.Assert;
import main.EmployeePayrollService.IOService;

class EmployeePayrollServiceTest {

	@Test
	public void given3EmployeesWhenWrittenToFileShowMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = {
				new EmployeePayrollData(1,"Vanshika",19035.9),
				new EmployeePayrollData(2,"Krati",19735.9),
				new EmployeePayrollData(3,"Nishu",19935.9),
		};
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(IOService.File_IO);
		employeePayrollService.printData(IOService.File_IO);
		//long entries = employeePayrollService.countEntries(IOService.File_IO);
		//Assert.assertEquals(3, entries);
	}

	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMachEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terisa",4000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}
}
