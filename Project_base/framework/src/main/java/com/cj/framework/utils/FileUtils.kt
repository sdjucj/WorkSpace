package com.cj.framework.utils

import com.cj.framework.listeners.OnProgressUpdateListener
import java.io.*
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset

/**
 * 文件处理工具
 *
 * @author CJ
 * @date 2021/5/6 9:41
 */

private const val BUFFER_SIZE = 8 * 1024

/**
 * 通过路径获取文件
 *
 * @param filePath 文件路径
 * @return 文件
 */
fun getFileByPath(filePath: String?): File? {
    return if (filePath.isNullOrEmpty()) {
        null
    } else {
        File(filePath)
    }
}

/**
 * 文件是否存在
 *
 * @param filePath 文件路径
 * @return true-存在
 */
fun isFileExists(filePath: String?): Boolean {
    val file = getFileByPath(filePath)
    return isFileExists(file)
}

/**
 * 文件是否存在
 *
 * @param file 文件
 * @return true-存在
 */
fun isFileExists(file: File?): Boolean {
    return file?.exists() ?: false
}

/**
 * 是否是文件夹
 *
 * @param filePath 文件路径
 * @return true - 是文件夹
 */
fun isDirectory(filePath: String?): Boolean {
    return isDirectory(getFileByPath(filePath))
}

/**
 * 是否是文件夹
 *
 * @param file 文件
 * @return true - 是文件夹
 */
fun isDirectory(file: File?): Boolean {
    return isFileExists(file) && file?.isDirectory ?: false
}

/**
 * 是否是文件
 *
 * @param filePath 文件路径
 * @return true - 是文件
 */
fun isFile(filePath: String?): Boolean {
    return isFile(getFileByPath(filePath))
}

/**
 * 是否是文件
 *
 * @param file 文件
 * @return true - 是文件
 */
fun isFile(file: File?): Boolean {
    return isFileExists(file) && file?.isFile ?: false
}

/**
 * 创建文件夹
 *
 * @param filePath 文件夹路径
 * @return true - 创建成功
 */
fun createDirectory(filePath: String?): Boolean {
    return createDirectory(getFileByPath(filePath))
}

/**
 * 创建文件夹
 *
 * @param file 文件夹
 * @return true - 创建成功
 */
fun createDirectory(file: File?): Boolean {
    return file?.run {
        if (exists()) {
            isDirectory
        } else {
            mkdirs()
        }
    } ?: false
}

/**
 * 创建文件
 *
 * @param filePath 文件路径
 * @return true - 创建成功
 */
fun createFile(filePath: String?): Boolean {
    return createFile(getFileByPath(filePath))
}

/**
 * 创建文件
 *
 * @param file 文件路径
 * @return true - 创建成功
 */
fun createFile(file: File?): Boolean {
    return file?.run {
        if (exists()) {
            isFile
        } else {
            if (createDirectory(parentFile)) {
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    false
                }
            } else {
                false
            }
        }
    } ?: false
}

/**
 * 删除文件夹
 *
 * @param filePath 文件夹路径
 * @return true - 删除成功
 */
fun deleteDirectory(filePath: String?): Boolean {
    return deleteDirectory(getFileByPath(filePath))
}

/**
 * 删除文件夹
 *
 * @param file 文件夹
 * @return true - 删除成功
 */
fun deleteDirectory(file: File?): Boolean {
    return file?.run {
        if (exists()) {
            if (isDirectory) {
                val files = listFiles()
                files?.run {
                    this.forEach {
                        if (it.isFile) {
                            if (!deleteFile(it)) {
                                return false
                            }
                        } else {
                            if (!deleteDirectory(it)) {
                                return false
                            }
                        }
                    }
                }
                delete()
            } else {
                false
            }
        } else {
            true
        }
    } ?: false
}

/**
 * 删除文件
 *
 * @param filePath 文夹路径
 * @return true - 删除成功
 */
fun deleteFile(filePath: String?): Boolean {
    return deleteFile(getFileByPath(filePath))
}

/**
 * 删除文件
 *
 * @param file 文件路径
 * @return true - 删除成功
 */
fun deleteFile(file: File?): Boolean {
    return file?.run {
        if (exists() && isFile) {
            delete()
        } else {
            true
        }
    } ?: false
}

/**
 * 删除
 *
 * @param path 路径
 * @return true-删除成功
 */
fun delete(path: String?): Boolean {
    return delete(getFileByPath(path))
}

/**
 * 删除
 *
 * @param file 文件
 * @return true-删除成功
 */
fun delete(file: File?): Boolean {
    return file?.run {
        if (isDirectory) {
            deleteDirectory(this)
        } else {
            deleteFile(this)
        }
    } ?: false
}

/**
 * 获取文件夹长度
 *
 * @param filePath 文件夹路径
 * @return 长度单位byte
 */
fun getDirectoryLength(filePath: String?): Long {
    return getDirectoryLength(getFileByPath(filePath))
}

/**
 * 获取文件夹长度
 *
 * @param file 文件夹
 * @return 长度单位byte
 */
fun getDirectoryLength(file: File?): Long {
    return file?.run {
        if (file.exists() && isDirectory) {
            val files = listFiles()
            var length: Long = 0
            files?.run {
                this.forEach {
                    length += if (it.isFile) {
                        getFileLength(it)
                    } else {
                        getDirectoryLength(it)
                    }
                }
            }
            length
        } else {
            0
        }
    } ?: 0

}

/**
 * 获取文件长度
 *
 * @param filePath 文件路径
 * @return 长度单位byte
 */
fun getFileLength(filePath: String?): Long {
    return getFileLength(getFileByPath(filePath))
}

/**
 * 获取文件长度
 *
 * @param file 文件路径
 * @return 长度单位byte
 */
fun getFileLength(file: File?): Long {
    return file?.run {
        if (exists() && isFile) {
            length()
        } else {
            0
        }
    } ?: 0
}

/**
 * 获取长度
 *
 * @param path 路径
 * @return 长度单位byte
 */
fun getLength(path: String?): Long {
    return getLength(getFileByPath(path))
}

/**
 * 获取长度
 *
 * @param file 文件
 * @return 长度单位byte
 */
fun getLength(file: File?): Long {
    return file?.run {
        if (exists()) {
            if (isDirectory) {
                getDirectoryLength(this)
            } else {
                getFileLength(this)
            }
        } else {
            0
        }
    } ?: 0
}

//*************写文件 start **********************
fun write(filePath: String?, bytes: ByteArray): Boolean {
    return write(filePath, bytes, false)
}

fun write(filePath: String?, bytes: ByteArray, append: Boolean): Boolean {
    return write(filePath, bytes, append, null)
}

fun write(filePath: String?, bytes: ByteArray, listener: OnProgressUpdateListener?): Boolean {
    return write(filePath, bytes, false, listener)
}

fun write(
    filePath: String?,
    bytes: ByteArray,
    append: Boolean,
    listener: OnProgressUpdateListener?
): Boolean {
    return write(getFileByPath(filePath), bytes, append, listener)
}

fun write(file: File?, bytes: ByteArray): Boolean {
    return write(file, bytes, false)
}

fun write(file: File?, bytes: ByteArray, append: Boolean): Boolean {
    return write(file, bytes, append, null)
}

fun write(file: File?, bytes: ByteArray, listener: OnProgressUpdateListener?): Boolean {
    return write(file, bytes, false, listener)
}

fun write(
    file: File?,
    bytes: ByteArray,
    append: Boolean,
    listener: OnProgressUpdateListener?
): Boolean {
    return write(file, ByteArrayInputStream(bytes), append, listener)
}


fun write(filePath: String?, inputStream: InputStream): Boolean {
    return write(filePath, inputStream, false)
}

fun write(filePath: String?, inputStream: InputStream, append: Boolean): Boolean {
    return write(filePath, inputStream, append, null)
}

fun write(file: File?, inputStream: InputStream): Boolean {
    return write(file, inputStream, false)
}

fun write(file: File?, inputStream: InputStream, append: Boolean): Boolean {
    return write(file, inputStream, append, null)
}

fun write(
    filePath: String?,
    inputStream: InputStream,
    listener: OnProgressUpdateListener?
): Boolean {
    return write(filePath, inputStream, false, listener)
}

fun write(
    filePath: String?,
    inputStream: InputStream,
    append: Boolean,
    listener: OnProgressUpdateListener?
): Boolean {
    return write(getFileByPath(filePath), inputStream, append, listener)
}

fun write(
    file: File?,
    inputStream: InputStream,
    listener: OnProgressUpdateListener?
): Boolean {
    return write(file, inputStream, false, listener)
}

/**
 * 写文件
 *
 * @param file 文件
 * @param inputStream 输入流
 * @param append 追加
 * @param listener 写入监听
 * @return true-成功
 */
fun write(
    file: File?,
    inputStream: InputStream,
    append: Boolean,
    listener: OnProgressUpdateListener?
): Boolean {
    return file?.run {
        if (!createFile(file)) {
            return false
        }
        var bos: BufferedOutputStream? = null
        var bis: BufferedInputStream? = null
        try {
            bis = BufferedInputStream(inputStream, BUFFER_SIZE)
            bos = BufferedOutputStream(FileOutputStream(file, append), BUFFER_SIZE)
            val totalSize = bis.available()
            var currentSize = 0.0
            listener?.onProgressUpdate(currentSize)
            val data = ByteArray(BUFFER_SIZE)
            var len: Int
            while ((bis.read(data).also { len = it }) != -1) {
                bos.write(data, 0, len)
                currentSize += len
                listener?.onProgressUpdate(currentSize / totalSize)
            }
            bos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos?.flush()
                bos?.close()
                bis?.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        true
    } ?: false
}

fun writeByChannel(filePath: String?, bytes: ByteArray, force: Boolean): Boolean {
    return writeByChannel(filePath, bytes, false, force)
}

fun writeByChannel(filePath: String?, bytes: ByteArray, append: Boolean, force: Boolean): Boolean {
    return writeByChannel(filePath, bytes, append, force)
}


fun writeByChannel(file: File?, bytes: ByteArray, force: Boolean): Boolean {
    return writeByChannel(file, bytes, false, force)
}

fun writeByChannel(
    file: File?,
    bytes: ByteArray,
    append: Boolean,
    force: Boolean
): Boolean {
    return writeByChannel(file, ByteArrayInputStream(bytes), append, force)
}


fun writeByChannel(filePath: String?, inputStream: InputStream, force: Boolean): Boolean {
    return writeByChannel(filePath, inputStream, false, force)
}

fun writeByChannel(
    filePath: String?,
    inputStream: InputStream,
    append: Boolean,
    force: Boolean
): Boolean {
    return writeByChannel(getFileByPath(filePath), inputStream, append, force)
}


fun writeByChannel(
    file: File?,
    inputStream: InputStream,
    force: Boolean
): Boolean {
    return writeByChannel(file, inputStream, false, force)
}

/**
 * 写文件
 *
 * @param file 文件
 * @param inputStream 输入流
 * @param append 追加
 * @param force 写入（类似flush）
 * @return true-成功
 */
fun writeByChannel(
    file: File?,
    inputStream: InputStream,
    append: Boolean,
    force: Boolean
): Boolean {
    return file?.run {
        if (!createFile(file)) {
            return false
        }
        var fosChannel: FileChannel? = null
        try {
            fosChannel = FileOutputStream(file, append).channel
            fosChannel.position(fosChannel.size())
            fosChannel.write(ByteBuffer.wrap(inputStream.readBytes()))
            if (force) {
                fosChannel.force(true)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fosChannel?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        true
    } ?: false
}


fun writeByMap(filePath: String?, bytes: ByteArray, force: Boolean): Boolean {
    return writeByMap(filePath, bytes, false, force)
}

fun writeByMap(filePath: String?, bytes: ByteArray, append: Boolean, force: Boolean): Boolean {
    return writeByMap(filePath, bytes, append, force)
}


fun writeByMap(file: File?, bytes: ByteArray, force: Boolean): Boolean {
    return writeByMap(file, bytes, false, force)
}

fun writeByMap(
    file: File?,
    bytes: ByteArray,
    append: Boolean,
    force: Boolean
): Boolean {
    return writeByMap(file, ByteArrayInputStream(bytes), append, force)
}


fun writeByMap(filePath: String?, inputStream: InputStream, force: Boolean): Boolean {
    return writeByChannel(filePath, inputStream, false, force)
}

fun writeByMap(
    filePath: String?,
    inputStream: InputStream,
    append: Boolean,
    force: Boolean
): Boolean {
    return writeByMap(getFileByPath(filePath), inputStream, append, force)
}


fun writeByMap(
    file: File?,
    inputStream: InputStream,
    force: Boolean
): Boolean {
    return writeByMap(file, inputStream, false, force)
}

/**
 * 写文件
 *
 * @param file 文件
 * @param inputStream 输入流
 * @param append 追加
 * @param force 写入（类似flush）
 * @return true-成功
 */
fun writeByMap(
    file: File?,
    inputStream: InputStream,
    append: Boolean,
    force: Boolean
): Boolean {
    return file?.run {
        if (!createFile(file)) {
            return false
        }
        var fosChannel: FileChannel? = null
        try {
            val bytes = inputStream.readBytes()
            fosChannel = FileOutputStream(file, append).channel
            val mapped: MappedByteBuffer = fosChannel.map(
                FileChannel.MapMode.READ_WRITE,
                fosChannel.size(),
                bytes.size.toLong()
            )
            mapped.put(bytes)
            if (force) {
                mapped.force()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fosChannel?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        true
    } ?: false
}

fun write(filePath: String?, content: String): Boolean {
    return write(filePath, content, false)
}

fun write(filePath: String?, content: String, append: Boolean): Boolean {
    return write(getFileByPath(filePath), content, append)
}

fun write(file: File?, content: String): Boolean {
    return write(file, content, false)
}

/**
 * 写文件
 *
 * @param file 文件
 * @param content 内容
 * @param append 追加
 * @return true-成功
 */
fun write(file: File?, content: String, append: Boolean): Boolean {
    return file?.run {
        if (!createFile(file)) {
            return false
        }
        var bw: BufferedWriter? = null
        try {
            bw = BufferedWriter(FileWriter(file, append))
            bw.write(content)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bw?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        true
    } ?: false
}
//*************写文件 end **********************

//*************读文件 start **********************
fun readByChannel(filePath: String?): ByteArray? {
    return readByChannel(getFileByPath(filePath))
}

fun readByChannel(file: File?): ByteArray? {
    if (!createFile(file)) {
        return null
    }
    var channel: FileChannel? = null
    try {
        channel = RandomAccessFile(file, "r").channel
        val buffer: ByteBuffer = ByteBuffer.allocate(channel.size().toInt())
        while (true) {
            if (channel.read(buffer) <= 0) {
                break
            }
        }
        return buffer.array()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        try {
            channel?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun readByMap(filePath: String?): ByteArray? {
    return readByMap(getFileByPath(filePath))
}

fun readByMap(file: File?): ByteArray? {
    if (!createFile(file)) {
        return null
    }
    var channel: FileChannel? = null
    try {
        channel = RandomAccessFile(file, "r").channel
        val size: Int = channel.size().toInt()
        val mapped: MappedByteBuffer =
            channel.map(FileChannel.MapMode.READ_ONLY, 0, size.toLong()).load()
        val result = ByteArray(size)
        mapped.get(result, 0, size)
        return result
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        try {
            channel?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun readByStream(filePath: String?): ByteArray? {
    return readByStream(filePath, null)
}

fun readByStream(filePath: String?, listener: OnProgressUpdateListener?): ByteArray? {
    return readByStream(getFileByPath(filePath), listener)
}

fun readByStream(file: File?): ByteArray? {
    return readByStream(file, null)
}

fun readByStream(file: File?, listener: OnProgressUpdateListener?): ByteArray? {
    if (!createFile(file)) {
        return null
    }
    var outStream: ByteArrayOutputStream? = null
    var inStream: InputStream? = null
    try {
        inStream = BufferedInputStream(FileInputStream(file), BUFFER_SIZE)
        outStream = ByteArrayOutputStream()
        val bytes = ByteArray(BUFFER_SIZE)
        val totalSize = inStream.available()
        var currentSize: Double = 0.0
        var len: Int
        listener?.onProgressUpdate(currentSize)
        while (inStream.read(bytes, 0, BUFFER_SIZE).also { len = it } != -1) {
            outStream.write(bytes, 0, len)
            currentSize += len
            listener?.onProgressUpdate(currentSize / totalSize)
        }
        return outStream.toByteArray()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        try {
            outStream?.close()
            inStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun read(filePath: String?): String {
    return read(filePath, null)
}

fun read(filePath: String?, listener: OnProgressUpdateListener?): String {
    return read(filePath, Charsets.UTF_8, listener)
}

fun read(filePath: String?, charset: Charset, listener: OnProgressUpdateListener?): String {
    return read(getFileByPath(filePath), charset, listener)
}

fun read(file: File?): String {
    return read(file, null)
}

fun read(file: File?, listener: OnProgressUpdateListener?): String {
    return read(file, Charsets.UTF_8, listener)
}

fun read(file: File?, charset: Charset, listener: OnProgressUpdateListener?): String {
    return readByStream(file, listener)?.let {
        String(it, charset)
    } ?: ""
}
//*************读文件 end **********************