package imaxct.signer.action;

import imaxct.signer.bean.Msg;
import imaxct.signer.dao.AccountDao;
import imaxct.signer.dao.TiebaDao;
import imaxct.signer.domain.Account;
import imaxct.signer.domain.Tieba;
import imaxct.signer.domain.User;
import imaxct.signer.function.AccountFunction;
import imaxct.signer.misc.Lib;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Account Actions
 * Created by maxct on 2016/8/18.
 */
@Controller
@RequestMapping("/Account")
@SessionAttributes({"user"})
public class AccountAction {

    private static final Logger logger = Logger.getLogger(AccountAction.class);

    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public String bindAccount(@RequestParam String bduss, ModelMap session, HttpServletRequest req){
        Msg m = new Msg();
        if (session.containsAttribute("user")){
            User user = (User) session.get("user");
            AccountFunction function = new AccountFunction();
            AccountDao accountDao = new AccountDao();
            Account account = new Account();
            account.setCookie(bduss.trim());
            account.setUserId(user);
            accountDao.create(account);
            account = accountDao.getAccount(bduss.trim());
            account = function.getAccountInfo(account);
            if (account==null){
                m.put("errcode", -1);
                m.put("errmsg", "cookie错误");
            }else{
                List<Tieba>list = function.getLikedTieba(account);
                if (list==null){
                    m.put("errcode", -1);
                    m.put("errmsg", "获取喜欢的贴吧时出错");
                }else{
                    account.setTiebaTotal(list.size());
                    accountDao.merge(account);
                    //accountDao.updateAccount(account);
                    TiebaDao tiebaDao = new TiebaDao();
                    for (Tieba tieba : list){
                        tiebaDao.create(tieba);
                    }
                    m.put("errcode", 0);
                    m.put("errmsg", "ok");
                    m.put("data", account);
                }
            }
            req.setAttribute("msg", Lib.gsonToString(m.msg));
            return "ajax";
        }
        m.put("errcode", -1);
        m.put("errmsg", "Access Denied");
        req.setAttribute("msg", Lib.gsonToString(m.msg));
        return "ajax";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listTieba(@RequestParam int id, ModelMap session, HttpServletRequest req){
        Msg m = new Msg();
        if (session.containsAttribute("user")) {
            AccountDao accountDao = new AccountDao();
            Account account = accountDao.getAccount(id);
            if (account == null){
                m.put("errcode", -2);
                m.put("errmsg", "f**k");
            }else{
                TiebaDao tiebaDao = new TiebaDao();
                List<Tieba>list = tiebaDao.getOnesTieba(account);
                m.put("errcode", 0);
                m.put("errmsg", "ok");
                m.put("total", (list==null||list.isEmpty())?0:list.size());
                m.put("list", list);
            }
            req.setAttribute("msg", Lib.gsonToString(m.msg));
            return "ajax";
        }
        m.put("errcode", -1);
        m.put("errmsg", "Access Denied");
        req.setAttribute("msg", Lib.gsonToString(m.msg));
        return "ajax";
    }
}
