package ldpd.suso.service;

import ldpd.suso.entity.Sign;
import ldpd.suso.repository.SignRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@Slf4j  //로그 찍을 때 필요
public class SignService {

    @Autowired
    private SignRepository signRepository;

    // Service 메서드는 DB와 직접적인 연관

    public void registerSign(Sign sign, MultipartFile file) throws Exception {  //등록할 수어를 db에 저장
        if (file != null && !file.isEmpty() && !sign.getTitle().isEmpty() && !sign.getSign_desc().isEmpty()) {
            if(sign.getFilepath() != null)
                deleteFile(signRepository.findById(sign.getId()).get().getFilepath());

            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";  //파일 저장 경로

            UUID uuid = UUID.randomUUID();  //파일 이름에 붙일 랜덤 이름 생성

            String fileName = uuid + "_" + file.getOriginalFilename();  //랜덤 이름과 원래 파일 이름을 이어서 저장될 파일 이름 생성

            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            sign.setFilename(fileName);
            sign.setFilepath("/files/" + fileName);
            signRepository.save(sign);
        }

    }

    public void deleteFile(String filePath) {   //현재 static 폴더에 저장된 파일을 삭제해주는 메소드
        File deleteFile = new File("src/main/resources/static/" + filePath);

        if(deleteFile.exists()) {   //파일이 존재하면 삭제
            deleteFile.delete();
            log.info("파일을 삭제 했습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }


    // 게시글 리스트 처리
    public Page<Sign> signList(Pageable pageable){  //db에서 모든 수어 등록 글을 가져온다

        return signRepository.findAll(pageable);
    }

    public Page<Sign> signSearchList(String searchKeyword, Pageable pageable) { //db에서 검색한 수어 글을 가져온다

        return signRepository.findByTitleContaining(searchKeyword, pageable);
    }

    //특정 게시물 불러오기
    public Sign signDetail(Integer id){
        return signRepository.findById(id).get();
    }   //디테일 페이지에 필요한 수어 정보를 가져온다.

    //특정 게시물 삭제
    public void signDelete(Integer id) {    //db에서 수어 글을 삭제한다.

        deleteFile(signRepository.findById(id).get().getFilepath());
        signRepository.deleteById(id);
    }
}
