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
package com.apress.prospringmvc.bookstore.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by Iuliana Cosmina on 23/05/2020
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Map<String,String> filesMap = new HashMap<>();

	@Override
	public void store(MultipartFile file) {
		String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		if (file.isEmpty()) {
			throw new IllegalArgumentException("Failed to store empty file " + filename);
		}
		// This is a security check
		if (filename.contains("..")) {
			throw new IllegalArgumentException(
					"Cannot store file with relative path outside current directory "
							+ filename);
		}
		try (InputStream inputStream = file.getInputStream()) {
			String originalName = file.getOriginalFilename();
			Path tmpPath = File.createTempFile("bookstore", "-" + originalName).toPath();
			Files.copy(inputStream, tmpPath, StandardCopyOption.REPLACE_EXISTING);
			filesMap.put(originalName, tmpPath.toAbsolutePath().toString());
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed to store file " + filename);
		}
	}

	@Override
	public Resource loadAsResource(String filename) {
		int splitIndex = filename.indexOf("-");
		final String keyName = filename.substring(splitIndex + 1);
		Path filePath = new File(filesMap.get(keyName)).toPath();
		try {
			Resource storedFile = new UrlResource(filePath.toUri());
			if (storedFile.exists() || storedFile.isReadable()) {
				return storedFile;
			} else {
				throw new IllegalArgumentException(" Cannot read file " + filename);
			}
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(" Cannot read file " + filename);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		return filesMap.values().stream().map(loc -> new File(loc).toPath());
	}

	@PreDestroy
	@Override
	public void deleteAll() {
		filesMap.values().forEach(f -> new File(f).delete());
	}
}
