package org.aai.atc.crawler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecursiveCrawlerTest {
    private Path tempRoot;

    @BeforeAll
    public static void setUpClass() {

    }

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary root directory for testing
        tempRoot = Files.createTempDirectory("testRoot");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete the temporary directory and its contents
        Files.walk(tempRoot)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testCrawl() throws IOException {
        // Create a mock directory structure
        Path subDir1 = Files.createDirectory(tempRoot.resolve("subDir1"));
        Path subDir2 = Files.createDirectory(tempRoot.resolve("subDir2"));

        Path file1 = Files.createFile(tempRoot.resolve("file1.txt"));
        Path file2 = Files.createFile(subDir1.resolve("file2.txt"));
        Path file3 = Files.createFile(subDir2.resolve("file3.txt"));

        // Initialize the RecursiveCrawler
        RecursiveCrawler crawler = new RecursiveCrawler();

        // Perform the crawl
        List<File> result = crawler.crawl(tempRoot.toString());

        // Validate the results
        assertEquals(3, result.size(), "Should return all files in directory and subdirectories");

        // Check that all expected files are present in the result
        assertTrue(result.contains(file1.toFile()), "file1.txt should be included");
        assertTrue(result.contains(file2.toFile()), "file2.txt should be included");
        assertTrue(result.contains(file3.toFile()), "file3.txt should be included");
    }

    @Test
    void testCrawlEmptyDirectory() throws IOException {
        // Initialize the RecursiveCrawler
        RecursiveCrawler crawler = new RecursiveCrawler();

        // Perform the crawl on an empty directory
        List<File> result = crawler.crawl(tempRoot.toString());

        // Validate the results
        assertTrue(result.isEmpty(), "Should return an empty list for an empty directory");
    }

    @Test
    void testCrawlNonExistentDirectory() {
        // Initialize the RecursiveCrawler
        RecursiveCrawler crawler = new RecursiveCrawler();

        // Perform the crawl on a non-existent directory
        assertThrows(IOException.class, () -> crawler.crawl("nonExistentPath"),
                "Should throw an IOException for a non-existent directory");
    }

}