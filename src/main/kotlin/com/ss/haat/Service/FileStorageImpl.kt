package com.ss.haat.Service

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.DirectoryNotEmptyException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

@Service
class FileStorageImpl : FileStorage {
    final var root = Paths.get(".").normalize().toAbsolutePath()!!.toString()

    val rootLocation = Paths.get("$root/filestorage")

    init {
        Files.createDirectory(rootLocation)
    }
    override fun store(file: MultipartFile, name: String) {
//        var tk = LocalDateTime.now().toString()
//        tk = tk.replace(",","")
//        tk = tk.replace(".","")
//        tk = tk.replace(":","")
//        tk = Appsession.name+tk.replace("-","")+".jpg"
//        Appsession.file = tk
        Files.copy(file.inputStream, this.rootLocation.resolve(name))//file.originalFilename))
    }

    override fun loadFile(filename: String): Resource {
        val file = rootLocation.resolve(filename)
        val resource = UrlResource(file.toUri())

        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("FAIL!")
        }
    }

    override fun delete(name: String) {

        val file = rootLocation.resolve(name + ".png")
        val resource = UrlResource(file.toUri())
        val path = Paths.get(name + ".png")

        try {
            // Delete file or directory
            Files.delete(file)
            println("File or directory deleted successfully")
        } catch (ex: NoSuchFileException) {
            System.out.printf("No such file or directory: %s\n", path)
        } catch (ex: DirectoryNotEmptyException) {
            System.out.printf("Directory %s is not empty\n", path)
        } catch (ex: IOException) {
            println(ex)
        }

    }

    override fun deleteAll() {
        //FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init() {
        Files.createDirectory(rootLocation)
    }

    override fun loadFiles(): Stream<Path> {
        return Files.walk(this.rootLocation, 1)
                .filter { path -> !path.equals(this.rootLocation) }
                .map(this.rootLocation::relativize)
    }

}
