/*
Freeware License, some rights reserved

Copyright (c) 2020 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospringmvc.bookstore.web.view;

import com.apress.prospringmvc.bookstore.domain.Order;
import com.apress.prospringmvc.bookstore.domain.OrderDetail;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import java.util.Map;

/**
 * Created by Iuliana Cosmina on 23/08/2020
 */
public class OrderPdfView extends AbstractPdfView {
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment; filename=\"order.pdf\"");
		Order order = (Order) model.get("order");
		document.addTitle("Order :" + order.getId());
		document.add(new Paragraph("Order date: " + order.getOrderDate()));
		document.add(new Paragraph("Delivery date: " + order.getDeliveryDate()));
		Table orderDetails = new Table(4);
		orderDetails.addCell("Title");
		orderDetails.addCell("Price");
		orderDetails.addCell("#");
		orderDetails.addCell("Total");

		for (OrderDetail detail : order.getOrderDetails()) {
			orderDetails.addCell(detail.getBook().getTitle());
			orderDetails.addCell(detail.getBook().getPrice().toString());
			orderDetails.addCell(String.valueOf(detail.getQuantity()));
			orderDetails.addCell(detail.getPrice().toString());
		}
		document.add(orderDetails);

	}
}
