package layout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reda_benchraa.asn.Adapters.accountArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.Profile;
import com.example.reda_benchraa.asn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMembers extends Fragment {
    private static final String ARG_PARAM1 = "group";
    private Group group;
    String id;
    ListView members;
    accountArrayAdapter membersAdapter;
    List<Account> memberList = new ArrayList<>();

    public ViewMembers() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ViewMembers newInstance(Group group) {
        ViewMembers fragment = new ViewMembers();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            View v = getView();
            if (v != null) {
                members = (ListView) v.findViewById(R.id.listView_viewMembers);
                members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Account account = (Account) parent.getAdapter().getItem(position);
                        Intent t = new Intent(view.getContext(), Profile.class);
                        t.putExtra("account",account);
                        startActivityForResult(t,100);
                    }
                });
                if (getArguments() != null) {
                    group = (Group) getArguments().getSerializable(ARG_PARAM1);
                    id = Long.toString(group.getId());
                    if(!group.getMembers().isEmpty()){
                        memberList.addAll(group.getMembers());
                        membersAdapter = new accountArrayAdapter(getContext(), R.layout.account_item, memberList);
                        members.setAdapter(membersAdapter);
                    }else{
                        getGroup(getContext(), new HashMap<String, String>(), Utility.getProperty("API_URL",getContext())+"Groups/"+id+"/?include=members");
                    }
                }
            }
        }
    }
    private void getGroup(final Context context, final Map<String, String> map, final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    group = Group.mapJson((JSONObject) new JSONArray(response).get(0));
                    memberList.addAll(group.getMembers());
                    membersAdapter = new accountArrayAdapter(getContext(), R.layout.account_item, memberList);
                    members.setAdapter(membersAdapter);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Utility.getMap();
            }
        };
        queue.add(sr);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_members, container, false);
    }

}
