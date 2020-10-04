package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import com.apress.prospringmvc.bookstore.repository.OrderRepository;
import com.apress.prospringmvc.bookstore.service.FileStorageService;
import com.apress.prospringmvc.bookstore.util.WebFile;
import com.apress.prospringmvc.bookstore.web.support.SessionAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *  
 * @author Marten Deinum
 *
 */
@Controller
@RequestMapping("/customer/account")
@SessionAttributes(types = Account.class)
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
		private FileStorageService fileStorageService;

    @ModelAttribute("countries")
    public Map<String, String> countries(Locale currentLocale) {
        var countries = new TreeMap<String, String>();
        for (var locale : Locale.getAvailableLocales()) {
            countries.put(locale.getCountry(), locale.getDisplayCountry(currentLocale));
        }
        return countries;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.setRequiredFields("username", "emailAddress");
    }

    @GetMapping
    public String index(Model model, @SessionAttribute(value = LoginController.ACCOUNT_ATTRIBUTE, exposeAsModelAttribute = true) Account account) {
        model.addAttribute("orders", this.orderRepository.findByAccount(account));
			  model.addAttribute("fileOrders", getAsWebFiles());
        return "customer/account";
    }

	public List<WebFile> getAsWebFiles(){
		return fileStorageService.loadAll().map(
				path -> new WebFile(path.getFileName().toString(), MvcUriComponentsBuilder.fromMethodName(AccountController.class,
						"serveFileOrder", path.getFileName().toString()).build().toUri().toString()))
				.collect(Collectors.toList());

	}
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFileOrder(@PathVariable String filename) {

		Resource file = fileStorageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}


	 @PostMapping
	 @PutMapping
    public String update(@ModelAttribute Account account) {
        this.accountRepository.save(account);
        return "redirect:/customer/account";
    }

}
