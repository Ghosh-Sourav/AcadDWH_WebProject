<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
<jsp:include page="../commons/header.jsp" />
<jsp:include page="nav.jsp" />
<jsp:include page="tasks.jsp" />
<%
	Map<String, String> dfs = new LinkedHashMap<String, String>();

	dfs.put("departments", "dim");
	dfs.put("specialisations", "dim");
	dfs.put("students", "dim");
	dfs.put("teachers", "dim");
	dfs.put("courses", "dim");
	dfs.put("eval_areas", "dim");
	dfs.put("regtypes", "dim");
	dfs.put("times", "dim");

	dfs.put("sem_performance", "fact");
	dfs.put("spl_performance", "fact");
	dfs.put("stu_learning", "fact");
	dfs.put("teaching_quality", "fact");
	
%>
<h1>ETL Processes</h1>
<hr />

<table class="table table-hover" style="text-align: center;">
	<thead>
		<tr>
			<th style="text-align: center;" colspan="3">Dimension / Fact</th>
			<th style="text-align: center;" rowspan="2">Action</th>
		</tr>
		<tr>
			<th style="text-align: center;">Name</th>
			<th style="text-align: center;">Type</th>
			<th style="text-align: center;">Format</th>
		</tr>
	</thead>
	<tbody>
		<%
			for (Map.Entry<String, String> df : dfs.entrySet()) {
		%>
		<tr class="list-items">
			<td><%=df.getKey().toString().toUpperCase()%></td>
			<td><%="dim".equals(df.getValue()) ? "Dimension" : "Fact"%></td>
			<td><a href="/acaddwh/csv/header_<%=df.getValue() %>_<%=df.getKey() %>.csv" target="_blank">Download Header</a></td>
			<td>
				<form class="form-horizontal fileUploadForm" role="form"
					action="/acaddwh/ETLController?df=<%=df.getValue() + "_" + df.getKey()%>"
					enctype="multipart/form-data" method="post">
					<div class="form-group">
						<div class="col-sm-9">
							<input type="file" class="form-control fileInput" name="file"
								placeholder="Select file" accept=".csv" required="required" />
						</div>
						<div class="col-sm-3">
							<button type="submit" class="btn btn-info uploadButton">Upload</button>
						</div>
					</div>
				</form>
			</td>
		</tr>
		<%
			}
		%>
	</tbody>
</table>

<jsp:include page="../commons/footer.jsp" />