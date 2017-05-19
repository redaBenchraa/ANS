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
import com.example.reda_benchraa.asn.Adapters.deleteGroupArrayAdapter;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManageNestedGroups extends Fragment {
    private static final String ARG_PARAM1 = "group";
    private Group group;
    String id;
    ListView groups;
    deleteGroupArrayAdapter groupsAdapter;
    List<Group> groupList = new ArrayList<>();


    public ManageNestedGroups() {
        // Required empty public constructor
    }


    public static ManageNestedGroups newInstance(Group group) {
        ManageNestedGroups fragment = new ManageNestedGroups();
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
                groups = (ListView) v.findViewById(R.id.listView_manageNestedGRoups);
                groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Group group = (Group) parent.getAdapter().getItem(position);
                        Intent t = new Intent(view.getContext(), com.example.reda_benchraa.asn.group.class);
                        t.putExtra("group",group);
                        startActivityForResult(t,100);
                    }
                });
                if (getArguments() != null) {
                    group = (Group) getArguments().getSerializable(ARG_PARAM1);
                    id = Long.toString(group.getId());
                    if(!group.getMembers().isEmpty()){
                        groupList.addAll(group.getSubGroups());
                        groupsAdapter = new deleteGroupArrayAdapter(getContext(), R.layout.remove_group_item, groupList);
                        groups.setAdapter(groupsAdapter);
                    }else{
                        getGroup(getContext(), new HashMap<String, String>(), Utility.getProperty("API_URL",getContext())+"Groups/"+id+"/?include=subs");
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
                    if(!group.getSubGroups().isEmpty()){
                        groupList.addAll(group.getSubGroups());
                        groupsAdapter = new deleteGroupArrayAdapter(getContext(), R.layout.remove_group_item, groupList);
                        groups.setAdapter(groupsAdapter);
                    }
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
        return inflater.inflate(R.layout.fragment_manage_nested_groups, container, false);
    }


}
