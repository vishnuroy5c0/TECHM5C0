package com.aits.VehicleServicesManagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.aits.VehicleServicesManagement.entity.BillOfMaterial;
import com.aits.VehicleServicesManagement.entity.ProvidedServices;
import com.aits.VehicleServicesManagement.entity.ServiceAdvisor;
import com.aits.VehicleServicesManagement.entity.Service_Record;
import com.aits.VehicleServicesManagement.entity.TotalServices;
import com.aits.VehicleServicesManagement.repo.BillOfMaterialRepository;
import com.aits.VehicleServicesManagement.repo.ProvidedServicesRepository;
import com.aits.VehicleServicesManagement.repo.ServiceAdvisorRepository;
import com.aits.VehicleServicesManagement.repo.ServiceRecordRepository;
import com.aits.VehicleServicesManagement.repo.TotalServicesRepository;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	 @Autowired
	 ProvidedServicesRepository psr;
	 
	 @Autowired
	 ServiceAdvisorRepository sar;
	 
	 @Autowired
	 TotalServicesRepository tsr;
	 
	 
	 @Autowired 
	 ServiceRecordRepository srr;
	 
	 @Autowired
	 BillOfMaterialRepository bom;
	 
	 
	 
	 @GetMapping("/dueservices")
	 public List<TotalServices> getDueVehiclesInWeek(
	     @RequestParam("startDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, //yyyy-MM-dd sends the request like this 
	     //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate ---- this says that it takes date from parameter and converts into localdate
	     @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
	     
	      return tsr.findByReceivedDateBetweenAndStatus(startDate, endDate, "DUE");
	 }
	 
	 @GetMapping("/ongoingservices/{status}")
	 public List<TotalServices> getOngoing(@PathVariable String status) {
		  return (List<TotalServices>) tsr.findByStatus(status);
	 }
	 
	 @GetMapping("/completedservices/{status}")
	 public List<TotalServices> getCompleted(@PathVariable String status) {
		  return (List<TotalServices>) tsr.findByStatus(status);
	 }
	 
	 @PutMapping("/assignforservicing/{Service_id}/{Advisor_id}")
	 public TotalServices assignforAdvisor(@PathVariable Long Service_id,@PathVariable Long Advisor_id) {
		  Optional<TotalServices> serviceOptional=tsr.findById(Service_id);
		  TotalServices service=serviceOptional.get();
		  if("DUE".equals(service.getStatus())) {
		  service.setScheduled_date(LocalDate.now());
		  Optional<ServiceAdvisor> advisorOptional=sar.findById(Advisor_id);
		  ServiceAdvisor advisor=advisorOptional.get();
		  if("FREE".equals(advisor.getStatus())){
			  service.setService_advisor(advisor);
			  service.setStatus("ONGOING");
			  advisor.setStatus("OCCUPIED");
			  
		  }
		  }
		  return tsr.save(service);
		  		 
	 }
	 
	 @GetMapping("/servicerecord/{id}")
	 public Optional<Service_Record> getservicerecord(@PathVariable Long id) {
		 Optional<Service_Record> record=srr.findById(id);
		 return record;
	 }
	 
	 
	 // crud operations 
	 @PostMapping("/addservices")
	 public ProvidedServices insert(@RequestBody ProvidedServices ps) {
		 return psr.save(ps);
	 } // admin adds the services provided
	 

	 @PostMapping("/addserviceadvisors")
	 public ServiceAdvisor insert(@RequestBody ServiceAdvisor sa) {
		 return sar.save(sa);
	 } //admin adds the serviceadvisors
	 
	 
	 @PostMapping("/totalservices")
	 public TotalServices insert(@RequestBody TotalServices ts) {
		 return tsr.save(ts);	 
	 }
	 
	 @GetMapping("/getprovidedservices")
	 public List<ProvidedServices> getallprovidedservices(){
		 return psr.findAll();
	 }
	 
	 @GetMapping("/getserviceadvisors")
	 public List<ServiceAdvisor> getallserviceadvisors(){
		 return sar.findAll();
	 }
	 
	 @DeleteMapping("/deleteserviceadvisor/{id}")
	 public String deleteserviceadvisor(@PathVariable Long id) {
		 String str;
		 if (sar.existsById(id)) {
			   sar.deleteById(id);
			str="Deleted successfully";
			}
		 else {
			 str="ID not found"; 
	    }
		 return str;
	 }
	 
	 @DeleteMapping("/deleteitems")
	 public void deleteserviceitems() {
		 List<ProvidedServices> services=psr.findAll();
		 for(ProvidedServices ps:services) {
			 if(ps.getQuantity()==0) {
				 psr.deleteById(ps.getId());
			 }
		 }
	 }
 
	 
	@Transactional
	@PutMapping("/reducequantity")
	public void reducequantity() {
		List<BillOfMaterial> bills=bom.getAllBills();
		for(BillOfMaterial bill:bills) {
		      Long serviceId=bill.getService().getId();
			  int qty1=bill.getQuantity() ;//quantity used
			Optional<ProvidedServices> psOptional=psr.findById(serviceId); // quantity present
			ProvidedServices ps=psOptional.get();
			int qty2=ps.getQuantity();
			int qty=qty2-qty1; //updatedquantity
			ps.setQuantity(qty);
			psr.save(ps);
		}
	}
}
