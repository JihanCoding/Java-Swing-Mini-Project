package DBManager;

import java.util.ArrayList;

import DBManager.DAO;
import DBManager.MemberVO;

public class Controller {
	
	DAO dao = new DAO();
	int result = 0;
	String rsStr = null;
	ArrayList<MemberVO> rsSelAll;
	
//	public MemberVO VOData(String id, String pw, String name, int age, int score) {
//		MemberVO vo = new MemberVO(id,pw,name,age,score);
//		return vo;
//	}
	//회원가입
	public int contInsert(MemberVO vo)
	{
		// model DAO에 전달
		result = dao.insert(vo);
		return result;
	}
	public int contInsertWords(MemberVO vo)
	{
		// model DAO에 전달
		result = dao.insertWords(vo);
		return result;
	}
	//로그인
	public String contSelect(MemberVO vo)
	{
		rsStr = dao.select(vo);
		return rsStr;
	}
	public int selectScore(MemberVO vo)
	{
		result = dao.selectScore(vo);
		return result;
	}
	//수정
	public int contUpdate(MemberVO vo)
	{
		result = dao.update(vo);
		return result;
	}
	//삭제
	public int contDelete(MemberVO vo)
	{
		result = dao.delete(vo);
		return result;
	}
	//전체조회selectScore
	public ArrayList<MemberVO> contSelectAll()
	{
		rsSelAll = dao.selectAll();
		return rsSelAll;
	}
	public ArrayList<MemberVO> conselectWordsAll()
	{
		rsSelAll = dao.selectWordsAll();
		return rsSelAll;
	}
	
	public ArrayList<MemberVO> congetRanking()
	{
		rsSelAll = dao.getRanking();
		return rsSelAll;
	}
}
