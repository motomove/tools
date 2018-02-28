
/**
 *
 * springMVC 实现文件上传
 * 实现分块上传与整体上传
 */
public class springMVC-upload {

    /**
     * 分塊上傳
     * 檢查分塊 追加內容
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "blockUploads", headers = "Content-Range")
    public void uploadChunked(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNewName = request.getParameter("fileName");
        if(StringUtils.isBlank(fileNewName)){
            throw new EntityException("文件信息不合法");
        }

        String contentRange = request.getHeader("Content-Range");
        String begin = "", filePath = "";
        Matcher matcher2 = RANGE_PATTERN.matcher(contentRange);
        if (matcher2.matches()) {
            begin = matcher2.group(1);
        }
        MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) request;
        MultipartFile f = multiReq.getFile("Filedata");
        //得到文件存放的相对目录
        filePath = FileUtils.getFileSavePath(null, f.getContentType());
        String extensionName = FileUtils.getExtension(f.getOriginalFilename());
        fileNewName = fileNewName + "." + extensionName;
        filePath = filePath + fileNewName ;

        //得到文件存放的绝对目录
        String fileSaveRealPath =  FileUtils.getFileSaveRealPath(filePath);
        if ("0".equals(begin)) {
            File source = new File(fileSaveRealPath);
            f.transferTo(source);
        } else {
            Path file = Paths.get(fileSaveRealPath);
            Files.write(file, f.getBytes(), StandardOpenOption.APPEND);
        }
        UploadUtil.responseMsg(Return.CORRECT_STATUS, filePath, response, true);
    }

    /**
     * 文件上傳
     * @return
     */
    @RequestMapping(value = "blockUploads", headers = "!Content-Range")
    public void blockUploads(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mrequest.getFile("Filedata");
        try {
            //得到文件存放的相对目录
            String fileSavePath = FileUtils.getFileSavePath(null, file.getContentType());
            //得到文件存放的绝对目录
            String fileSaveRealPath =  FileUtils.getFileSaveRealPath(fileSavePath);
            String fileNewName = FileUtils.ceateFileNewName(file.getOriginalFilename());
            System.out.println("fileSaveRealPath = " + fileSaveRealPath);
            File f = new File(fileSaveRealPath + fileNewName);
            file.transferTo(f);
            UploadUtil.responseMsg(Return.CORRECT_STATUS, fileSavePath + fileNewName, response, true);
        } catch (Exception e) {
            e.printStackTrace();
            UploadUtil.responseMsg(Return.ERROR_REQUEST, "请求参数异常", response, false);
        }
    }
	

}
