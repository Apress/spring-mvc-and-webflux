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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Iuliana Cosmina on 23/08/2020
 */
public class OrderExcelView extends AbstractXlsView {
	@Override
	protected void buildExcelDocument(Map<String, Object> model, org.apache.poi.ss.usermodel.Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment; filename=\"order.xls\"");
		Order order = (Order) model.get("order");
		Sheet sheet = workbook.createSheet();
		sheet.createRow(1).createCell(0).setCellValue("Order: " + order.getId());
		sheet.createRow(2).createCell(0).setCellValue("Order Date: " + order.getOrderDate());
		sheet.createRow(3).createCell(0).setCellValue("Delivery Date: " + order.getDeliveryDate());
		sheet.createRow(4).createCell(0).setCellValue("Order: " + order.getId());

		Row header = sheet.createRow(5);
		header.createCell(0).setCellValue("Quantity");
		header.createCell(1).setCellValue("Title");
		header.createCell(2).setCellValue("Price");

		int row = 5;
		for (OrderDetail detail : order.getOrderDetails()) {
			row++;
			Row detailRow = sheet.createRow(row);
			detailRow.createCell(0).setCellValue(detail.getQuantity());
			detailRow.createCell(1).setCellValue(detail.getBook().getTitle());
			detailRow.createCell(2).setCellValue(detail.getPrice().doubleValue() * detail.getQuantity());
		}

		row++;
		Row footer = sheet.createRow(row);
		footer.createCell(0).setCellValue("Total");
		footer.createCell(1).setCellValue(order.getTotalOrderPrice().doubleValue());

	}
}
