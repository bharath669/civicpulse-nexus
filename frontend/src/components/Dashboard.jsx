import {useEffect,useState} from 'react'
import {dashboardApi,grievanceApi} from '../api/api.js'
import {ErrorBox,Stat,Status,Sla} from './Shared.jsx'
export default function Dashboard({onOpen}){
 const [summary,setSummary]=useState(null),[latest,setLatest]=useState([]),[error,setError]=useState(null)
 const load=()=>Promise.all([dashboardApi.summary(),grievanceApi.list()]).then(([s,l])=>{setSummary(s);setLatest(l.slice(0,8))}).catch(setError)
 useEffect(()=>{load()},[])
 if(error)return <ErrorBox error={error}/>
 if(!summary)return <p>Loading dashboard…</p>
 const g=latest[0]
 return <><div className="page-title"><div><small>OVERVIEW</small><h1>Registration & Redressal</h1></div><button className="secondary" onClick={load}>Refresh</button></div>
 <section className="stats">
  <Stat label="Registered Citizens" value={summary.registeredCitizens<0?'Unavailable':summary.registeredCitizens} hint="active records"/>
  <Stat label="Grievances / Month" value={summary.grievancesThisMonth} hint="filed this month"/>
  <Stat label="Resolution Rate" value={`${summary.resolutionRate}%`} hint="resolved + closed" tone="good"/>
  <Stat label="Open Grievances" value={summary.openGrievances} hint="needs action"/>
  <Stat label="SLA Breached" value={summary.slaBreachedOpen} hint="open & overdue" tone="bad"/>
  <Stat label="SLA Compliance" value={`${summary.slaCompliance}%`} hint="resolved within SLA" tone="good"/>
 </section>
 {g&&<section className="panel"><div className="panel-head"><h2>Latest Grievance Snapshot</h2><Status value={g.status}/></div>
 <p><b>{g.citizenName}</b> · {g.citizenCode} · Ward {g.wardNumber}</p><p>{g.grievanceNumber} · {g.category} · {g.severity}</p>
 <p>{g.title}</p><p>Department: {g.departmentName} · Officer: {g.assignedOfficer||'Not assigned'}</p>
 <p>SLA: {g.slaHours} hours · Due {new Date(g.slaDueAt).toLocaleString()} · <Sla value={g.slaStatus}/></p>
 <button onClick={()=>onOpen(g.id)}>View workflow</button></section>}
 <section className="panel"><h2>Recent Grievances</h2><div className="table-wrap"><table><thead><tr><th>Grievance No.</th><th>Citizen</th><th>Category</th><th>Department</th><th>Status</th><th>SLA</th></tr></thead>
 <tbody>{latest.map(x=><tr key={x.id} onClick={()=>onOpen(x.id)} className="clickable"><td>{x.grievanceNumber}</td><td>{x.citizenName}</td><td>{x.category}</td><td>{x.departmentName}</td><td><Status value={x.status}/></td><td><Sla value={x.slaStatus}/></td></tr>)}</tbody></table></div></section>
 </>}
