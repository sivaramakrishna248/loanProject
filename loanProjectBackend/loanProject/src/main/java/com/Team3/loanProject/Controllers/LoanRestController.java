package com.Team3.loanProject.Controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.sql.Date;

import com.Team3.loanProject.Entities.Applicant;
import com.Team3.loanProject.Repositories.ApplicantRepository;
import com.Team3.loanProject.Services.ApplicantService;
import com.Team3.loanProject.dto.CreateLoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.NamedParameterJdbcOperationsDependsOnPostProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class LoanRestController {

    @Autowired
    ApplicantRepository applicantRepository;

    @Autowired
    private ApplicantService applicantService;

    @RequestMapping(value = "/home")
    public String home(){
        return "Welcome";
    }

    @RequestMapping(value = "/loans", method = RequestMethod.GET)
    public List<Applicant> getLoans(){
        return applicantService.getall();
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @Transactional
    public Applicant postLoan(@RequestBody CreateLoanRequest request){

//        Applicant applicant= new Applicant();
//
//        applicant.setSSNNumber("q");
//        applicant.setFirstName("w");
//        applicant.setMiddleName("er");
//        applicant.setLastName("e");
//        applicant.setDateofBirth(Date.valueOf("1998-1-1"));
//        applicant.setDateSubmitted(Date.valueOf("2020-1-1"));
//        applicant.setMaritalStatus("request.getApplicantmaritalStatus()");
//        applicant.setAddressLine1("request.getApplicantaddrLine1()");
//        applicant.setAddressLine2("request.getApplicantaddrLine2()");
//        applicant.setCity("request.getApplicantcity()");
//        applicant.setState("request.getApplicantstate()");
//        applicant.setPostalCode("a");
//        applicant.setDescription("s");
//        applicant.setLoanAmount(874);
//        applicant.setLoanPurpose("a");
//        applicant.setAnnualSalary(4457);
//        applicant.setEmployername("request.getApplicantEmployerName()");
//        applicant.setEmployerAddress1("request.getApplicantEmployerAddr1()");
//        applicant.setEmployerAddress2("request.getApplicantEmployerAddr2()");
//        applicant.setEmployerCity("request.getApplicantEmployerCity()");
//        applicant.setEmployerState("request.getApplicantEmployerState()");
//        applicant.setEmployerPostalCode("a");
//        applicant.setDesignation("request.getApplicantdesignation()");
//        applicant.setMobile("a");
//        applicant.setHomePhone("s");
//        applicant.setOfficePhone("s");
//        applicant.setEmailAddress("s");
//        applicant.setWorkExperienceMonth(5);
//        applicant.setWorkExperienceYears(1);
//
//        applicant.setApplicationStatus("In Progress");
//        applicant.setScore("-");
//        applicant.setDeclineReason("In Progress");
//
//        Applicant a=applicantService.createApplicant(applicant);


        Applicant applicant= new Applicant();

        applicant.setSSNNumber(request.getApplicantSSN());
        applicant.setFirstName(request.getApplicantfirstName());
        applicant.setMiddleName(request.getApplicantmiddleName());
        applicant.setLastName(request.getApplicantlastName());
        applicant.setDateofBirth(Date.valueOf(request.getApplicantdob()));
        applicant.setDateSubmitted(Date.valueOf(LocalDate.now()));
        applicant.setMaritalStatus(request.getApplicantmaritalStatus());
        applicant.setAddressLine1(request.getApplicantaddrLine1());
        applicant.setAddressLine2(request.getApplicantaddrLine2());
        applicant.setCity(request.getApplicantcity());
        applicant.setState(request.getApplicantstate());
        applicant.setPostalCode(request.getApplicantpostalCode());
        applicant.setDescription(request.getApplicantdescription());
        applicant.setLoanAmount(request.getApplicantloanAmount());
        applicant.setLoanPurpose(request.getApplicantloanPurpose());
        applicant.setAnnualSalary(request.getApplicantAnnualSalary());
        applicant.setEmployername(request.getApplicantEmployerName());
        applicant.setEmployerAddress1(request.getApplicantEmployerAddr1());
        applicant.setEmployerAddress2(request.getApplicantEmployerAddr2());
        applicant.setEmployerCity(request.getApplicantEmployerCity());
        applicant.setEmployerState(request.getApplicantEmployerState());
        applicant.setEmployerPostalCode(request.getApplicantEmployerPostalCode());
        applicant.setDesignation(request.getApplicantdesignation());
        applicant.setHomePhone(request.getApplicanthomePhone());
        applicant.setOfficePhone(request.getApplicantofficePhone());
        applicant.setMobile(request.getApplicantmobile());
        applicant.setEmailAddress(request.getApplicantemail());
        applicant.setWorkExperienceMonth(request.getApplicantWorkExperienceMonth());
        applicant.setWorkExperienceYears(request.getApplicantWorkExperienceYears());

        applicant.setApplicationStatus("In Progress");
        applicant.setScore("-");
        applicant.setDeclineReason("In Progress");

        Applicant applicant1=applicantService.createApplicant(applicant);

        boolean valid=TRUE;

        LocalDate l = applicant.getDateofBirth().toLocalDate(); //specify year, month, date directly
        LocalDate now = LocalDate.now(); //gets localDate Period
        Period diff = Period.between(l, now); //difference between the dates is calculated

        if(applicant.getAnnualSalary()<10000){
            valid=FALSE;
            applicant.setDeclineReason("Declined at FrontEnd - Salary is less than $10000");
        }
        else if(diff.getYears()<18||diff.getYears()>65) {
            valid=FALSE;
            applicant.setDeclineReason("Declined at FrontEnd - Age not Age Group in 18-65");
        }
        else if(applicant.getWorkExperienceYears()<1&&applicant.getWorkExperienceMonth()<6){
            valid=FALSE;
            applicant.setDeclineReason("Declined at FrontEnd - Work Experience is less than 6 months");
        }

        int score=0;

        if(valid){
            score=applicantService.calculateScore(applicant);
            applicant.setScore(String.valueOf(score));
            if(score>10){ //threshold is 10 randomly, to be calculated by prasanna
                applicant.setApplicationStatus("Approved");
                applicant.setDeclineReason("-");
            }
            else{
                applicant.setApplicationStatus("Declined");

                applicant.setDeclineReason(" "); //prassanna take
            }
        }
        else{
            applicant.setScore("0");
            applicant.setApplicationStatus("Declined");
            applicant.setDeclineReason("Validations Failed");
        }

        applicantService.updateApplicant(applicant1);

        return null;
    }

    @RequestMapping(value = "/loans/{id}")
    public Applicant showLoan(@PathVariable("id") int id){
        return applicantService.getApplicant(id);
    }

    @RequestMapping(value = "/success")
    public String success(){
        return "Submitted";
    }

}
