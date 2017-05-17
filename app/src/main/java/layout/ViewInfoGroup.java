package layout;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reda_benchraa.asn.DAO.Utility;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;
import com.example.reda_benchraa.asn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class ViewInfoGroup extends Fragment {
    private static final String ARG_PARAM1 = "group";
    private String id;
    Group group;
    TextView groupName,creationDate ,ownerName,about,email;
    ImageView groupImage,ownerImage;


    public ViewInfoGroup() {
        // Required empty public constructor
    }
    public static ViewInfoGroup newInstance(Group group) {
        ViewInfoGroup fragment = new ViewInfoGroup();
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
        View v = getView();
        if(v!=null){
            groupName = (TextView) v.findViewById(R.id.groupInfo_groupName);
            creationDate = (TextView) v.findViewById(R.id.groupInfo_creationDate);
            about = (TextView) v.findViewById(R.id.groupInfo_groupAbout);
            ownerName = (TextView) v.findViewById(R.id.groupInfo_ownerName);
            email = (TextView) v.findViewById(R.id.groupInfo_ownerEmail);
            groupImage = (ImageView) v.findViewById(R.id.groupInfo_groupImage);
            ownerImage = (ImageView) v.findViewById(R.id.groupInfo_ownerImage);
            if (getArguments() != null) {
                group = (Group) getArguments().getSerializable(ARG_PARAM1);
                id = Long.toString(group.getId());
                if(group!=null){
                    groupName.setText(group.getName());
                    about.setText(group.getAbout());
                    if(group.getImage()!=null){
                        groupImage.setImageBitmap(BitmapFactory.decodeByteArray(group.getImage(),0,group.getImage().length));
                    }
                    if(group.getOwner()!=null){
                        ownerName.setText(group.getOwner().getFirstName()+" "+group.getOwner().getLastName());
                        email.setText(group.getOwner().getEmail());
                        if(group.getOwner().getProfilePicture()!=null){
                            ownerImage.setImageBitmap(BitmapFactory.decodeByteArray(group.getOwner().getProfilePicture(),0,group.getOwner().getProfilePicture().length));
                        }
                    }else{
                        getGroup(getContext(), new HashMap<String, String>(), Utility.getProperty("API_URL",getContext())+"Groups/"+id+"?include=owner");
                    }
                }
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_info_group, container, false);
    }
    private void getGroup(final Context context, final Map<String, String> map, final String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    group = Group.mapJson((JSONObject) new JSONArray(response).get(0));
                    //THIS DATA HERE ASIDE FROM OWNER INFO ARE JUST FOR TESTING PURPOSES
                    /***/
                    groupName.setText(group.getName());
                    about.setText(group.getAbout());
                    creationDate.setText(group.getCreationDate().toString());
                    if(group.getImage()!=null){
                        groupImage.setImageBitmap(BitmapFactory.decodeByteArray(group.getImage(),0,group.getImage().length));
                    }
                    /***/
                    ownerName.setText(group.getOwner().getFirstName()+" "+group.getOwner().getLastName());
                    email.setText(group.getOwner().getEmail());
                    if(group.getOwner().getProfilePicture()!=null){
                        ownerImage.setImageBitmap(BitmapFactory.decodeByteArray(group.getOwner().getProfilePicture(),0,group.getOwner().getProfilePicture().length));
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

}
