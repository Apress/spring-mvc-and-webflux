package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.service.FileStorageService;
import com.apress.prospringmvc.bookstore.web.UploadOrderForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller to handle file uploads.
 * 
 * @author Marten Deinum
 */
@Controller
public class UploadOrderController {

	private final Logger logger = LoggerFactory.getLogger(UploadOrderController.class);
	private final FileStorageService fileStorageService;

	public UploadOrderController(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}


	@PostMapping(path = "/order/upload")
	public String handleUpload(UploadOrderForm form, RedirectAttributes redirectAttributes) {
		logFile(form.getOrder().getName(), form.getOrder().getSize());
		fileStorageService.store(form.getOrder());
		redirectAttributes.addFlashAttribute("message",  "Order was successfully uploaded!");
		return "redirect:/customer/account";
	}

	private void logFile(String name, long size) {
		this.logger.info("Received order: {}, size {}", name, size);
	}

	/* Uploading Files directly */
    /*@PostMapping(value = "/order/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleUpload(@RequestParam("order") final MultipartFile file, RedirectAttributes redirectAttributes) {
			fileStorageService.store(file);
			redirectAttributes.addFlashAttribute("message",  "Order was successfully uploaded!");
        return "redirect:/customer/account";
    }*/

}
